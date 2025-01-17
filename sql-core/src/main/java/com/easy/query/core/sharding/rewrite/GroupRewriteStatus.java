package com.easy.query.core.sharding.rewrite;


import java.util.Objects;

/**
 * create time 2023/4/30 23:08
 * 文件说明
 *
 * @author xuejiaming
 */
public class GroupRewriteStatus {
    public static int DEFAULT_GROUP_BEHAVIOR=GroupAvgBehaviorEnum.AVG.getCode()|GroupAvgBehaviorEnum.COUNT.getCode()|GroupAvgBehaviorEnum.SUM.getCode();
    private final int tableIndex;
    private final String propertyName;
    public int behavior=DEFAULT_GROUP_BEHAVIOR;
    public GroupRewriteStatus(int tableIndex,String propertyName){

        this.tableIndex = tableIndex;
        this.propertyName = propertyName;
    }

    public int getTableIndex() {
        return tableIndex;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean hasBehavior(GroupAvgBehaviorEnum easyBehavior){
        return (behavior& easyBehavior.getCode())== easyBehavior.getCode();
    }

    public boolean addBehavior(GroupAvgBehaviorEnum easyBehavior){
        if(hasBehavior(easyBehavior)){
            return false;
        }else{
            behavior=behavior|easyBehavior.getCode();
            return true;
        }
    }
    public boolean removeBehavior(GroupAvgBehaviorEnum easyBehavior){
        if(hasBehavior(easyBehavior)){
            behavior=behavior&~easyBehavior.getCode();
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupRewriteStatus that = (GroupRewriteStatus) o;
        return tableIndex == that.tableIndex && Objects.equals(propertyName, that.propertyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableIndex, propertyName);
    }
}
