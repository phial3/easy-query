package com.easy.query.core.config;

import com.easy.query.core.util.StringUtil;

/**
 * @FileName: DefaultEasyQueryDialect.java
 * @Description: 文件说明
 * @Date: 2023/2/12 09:22
 * @author xuejiaming
 */
public abstract class AbstractDialect implements IDialect {
    protected abstract String getQuoteStart();

    protected abstract String getQuoteEnd();

    @Override
    public String getQuoteName(String keyword) {
        String quoteStart = getQuoteStart();
        String quoteEnd = getQuoteEnd();
        StringBuilder keyBuilder = new StringBuilder();
        if(quoteStart!=null&&!StringUtil.startsWith(keyword,quoteStart)){
            keyBuilder.append(quoteStart);
        }
        keyBuilder.append(keyword);
        if(quoteEnd!=null&&!StringUtil.endsWith(keyword,quoteEnd)){
            keyBuilder.append(quoteEnd);
        }
        return keyBuilder.toString();
    }
}
