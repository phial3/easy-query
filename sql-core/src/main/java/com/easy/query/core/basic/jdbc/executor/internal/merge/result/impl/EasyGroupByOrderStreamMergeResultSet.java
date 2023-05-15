package com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl;

import com.easy.query.core.basic.jdbc.executor.internal.merge.result.StreamResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.aggregation.AggregationUnitFactory;
import com.easy.query.core.basic.jdbc.executor.internal.merge.segment.PropertyGroup;
import com.easy.query.core.exception.EasyQuerySQLException;
import com.easy.query.core.expression.segment.AggregationColumnSegment;
import com.easy.query.core.expression.segment.SqlSegment;
import com.easy.query.core.sharding.context.StreamMergeContext;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.ShardingStreamResultSet;
import com.easy.query.core.util.EasyCollectionUtil;
import com.easy.query.core.util.ClassUtil;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * create time 2023/4/27 23:11
 * 文件说明
 *
 * @author xuejiaming
 */
public class EasyGroupByOrderStreamMergeResultSet implements ShardingStreamResultSet {
    private final StreamMergeContext streamMergeContext;
    private final List<StreamResultSet> streamResults;
    private final Queue<StreamResultSet> queue;
    private final ResultSetMetaData resultSetMetaData;
    private StreamResultSet currentStreamResult;
    private final List<Object> currentRow;
    private final int columnCount;
    //数组下标对应select的下标,值为1的表示他是Group列0表示为func列
    private final int[] selectColumns;
    private List<Object> currentGroupValues;
    private boolean skipFirst;

    private boolean wasNull;
    private boolean closed = false;

    public EasyGroupByOrderStreamMergeResultSet(StreamMergeContext streamMergeContext, List<StreamResultSet> streamResults) throws SQLException {

        this.streamMergeContext = streamMergeContext;
        this.streamResults = streamResults;
        this.queue = new PriorityQueue<>(streamResults.size());
        this.skipFirst = true;
        setOrderStreamResult();
        this.resultSetMetaData = getResultSetMetaData();
        this.columnCount = resultSetMetaData.getColumnCount();
        this.currentRow = new ArrayList<>(columnCount);

        List<PropertyGroup> groups = streamMergeContext.getGroups();
        this.selectColumns = new int[streamMergeContext.getSelectColumns().getSqlSegments().size()];
        for (PropertyGroup group : groups) {
            int columnIndex = group.columnIndex();
            if (columnIndex >= 0) {
                selectColumns[columnIndex] = 1;
            }
        }
    }

    private void setOrderStreamResult() throws SQLException {
        for (StreamResultSet streamResult : streamResults) {
            EasyOrderStreamMergeResultSet easyOrderStreamMergeResult = new EasyOrderStreamMergeResultSet(streamMergeContext, streamResult);
            if (easyOrderStreamMergeResult.hasElement()) {
                easyOrderStreamMergeResult.skipFirst();
                queue.offer(easyOrderStreamMergeResult);
            }
        }
        currentStreamResult = queue.isEmpty() ? EasyCollectionUtil.firstOrNull(streamResults) : queue.peek();
        currentGroupValues = queue.isEmpty() ? Collections.emptyList() : new GroupValue(streamMergeContext, currentStreamResult).getGroupValues();
    }

    private ResultSetMetaData getResultSetMetaData() throws SQLException {
        return currentStreamResult.getMetaData();
    }

    @Override
    public boolean hasElement() {
        return currentStreamResult.hasElement();
    }

    @Override
    public boolean skipFirst() {
        return skipFirst;
    }

    @Override
    public boolean next() throws SQLException {
        currentRow.clear();
        if (queue.isEmpty()) {
            return false;
        }
        if (skipFirst) {
            next0();
        }
        if (aggregateCurrentGroupByRowAndNext()) {
            currentGroupValues = new GroupValue(streamMergeContext, currentStreamResult).getGroupValues();
        }
        return true;

    }

    private boolean next0() throws SQLException {

        if (queue.isEmpty()) {
            return false;
        }
        if (skipFirst) {
            skipFirst = false;
            return true;
        }
        StreamResultSet first = queue.poll();
        if (first.next()) {
            queue.offer(first);
        }
        if (queue.isEmpty()) {
            return false;
        }
        currentStreamResult = queue.peek();
        return true;
    }


    private boolean aggregateCurrentGroupByRowAndNext() throws SQLException {
        boolean result = false;
        boolean cachedRow = false;
        List<AggregateValue> aggregationValues = createAggregationUnitValues();
        while (currentGroupValues.equals(new GroupValue(streamMergeContext, currentStreamResult).getGroupValues())) {
            aggregate(aggregationValues);
            if (!cachedRow) {
                cacheCurrentRow();
                cachedRow = true;
            }
            result = next0();
            if (!result) {
                break;
            }
        }
        setAggregationValueToCurrentRow(aggregationValues);
        return result;
    }

    private void setAggregationValueToCurrentRow(List<AggregateValue> aggregationValues) {
        for (AggregateValue aggregationValue : aggregationValues) {
            currentRow.set(aggregationValue.getColumnIndex(), aggregationValue.getAggregationUnit().getResult());
        }
    }

    private void aggregate(List<AggregateValue> aggregationValues) throws SQLException {
        for (AggregateValue aggregationValue : aggregationValues) {
            Comparable<?> value = getAggregationValue(aggregationValue.getColumnIndex());
            aggregationValue.getAggregationUnit().merge(Collections.singletonList(value));
        }
    }

    private Comparable<?> getAggregationValue(int columnIndex) throws SQLException {
        Object result = currentStreamResult.getObject(columnIndex + 1);
        if (null == result || result instanceof Comparable) {
            return (Comparable<?>) result;
        }
        throw new EasyQuerySQLException("aggregation value must implements comparable");
    }

    private List<AggregateValue> createAggregationUnitValues() {
        List<SqlSegment> sqlSegments = streamMergeContext.getSelectColumns().getSqlSegments();
        ArrayList<AggregateValue> aggregationUnits = new ArrayList<>(columnCount);
        for (int i = 0; i < selectColumns.length; i++) {
            boolean aggregateColumn = selectColumns[i] == 0;
            if (aggregateColumn) {
                SqlSegment sqlSegment = sqlSegments.get(i);
                if (!(sqlSegment instanceof AggregationColumnSegment)) {
                    throw new UnsupportedOperationException("unknown aggregate column:" + ClassUtil.getInstanceSimpleName(sqlSegment));
                }
                AggregationColumnSegment aggregationColumnSegment = (AggregationColumnSegment) sqlSegment;
                aggregationUnits.add(new AggregateValue(i, AggregationUnitFactory.create(aggregationColumnSegment.getAggregationType())));
            }
        }
        return aggregationUnits;
    }


    private void cacheCurrentRow() throws SQLException {
        for (int i = 0; i < columnCount; i++) {
            currentRow.add(currentStreamResult.getObject(i + 1));
        }
    }

//    private boolean equalWithGroupValues() {
//        List<Object> current = new GroupValue(streamMergeContext, queue.peek()).getGroupValues();
//        for (int i = 0; i < currentGroupValues.size(); i++) {
//            if (!Objects.equals(currentGroupValues.get(i), current.get(i))) {
//                return false;
//            }
//        }
//        return true;
//    }

    private void setWasNull(boolean wasNull) {
        this.wasNull = wasNull;
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        return value;
    }

    @Override
    public boolean wasNull() throws SQLException {
        return wasNull;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return resultSetMetaData;
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        return (SQLXML) value;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        return (Timestamp) value;
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        return (Time) value;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        return (String) value;
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        return (Date) value;
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        if (value == null) {
            return 0;
        }
        return ((BigDecimal) value).shortValue();
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        if (value == null) {
            return 0;
        }
        return ((BigDecimal) value).longValue();
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        if (value == null) {
            return 0;
        }
        return ((BigDecimal) value).intValue();
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        if (value == null) {
            return 0;
        }
        return ((BigDecimal) value).floatValue();
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        if (value == null) {
            return 0;
        }
        return ((BigDecimal) value).doubleValue();
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        return (Clob) value;
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        if (value == null) {
            return 0;
        }
        return (byte) value;
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        if (value == null) {
            return new byte[0];
        }
        return (byte[]) value;
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        if (value == null) {
            return false;
        }
        return (boolean) value;
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        return (Blob) value;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        Object value = currentRow.get(columnIndex - 1);
        setWasNull(value == null);
        return (BigDecimal) value;
    }

    @Override
    public void close() throws Exception {
        if (closed) {
            return;
        }
        closed = true;
        currentRow.clear();
        for (StreamResultSet streamResult : streamResults) {
            streamResult.close();
        }
    }
}