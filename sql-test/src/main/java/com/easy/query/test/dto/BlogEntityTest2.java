package com.easy.query.test.dto;

import com.easy.query.core.annotation.Column;
import lombok.Data;
import org.junit.Ignore;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * create time 2023/3/30 21:32
 * 文件说明
 *
 * @author xuejiaming
 */
@Ignore
@Data
public class BlogEntityTest2 {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 博客链接
     */
    @Column("my_url")
    private String url;
    /**
     * 点赞数
     */
    private Integer star;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    /**
     * 评分
     */
    private BigDecimal score;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 排序
     */
    private BigDecimal order;
    /**
     * 是否置顶
     */
    private Boolean isTop;
    /**
     * 是否置顶
     */
    private Boolean top;
}
