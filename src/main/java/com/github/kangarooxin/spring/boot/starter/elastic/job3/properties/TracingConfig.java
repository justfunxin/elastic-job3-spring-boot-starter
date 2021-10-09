package com.github.kangarooxin.spring.boot.starter.elastic.job3.properties;

import lombok.Data;

/**
 * @author kangaroo_xin
 */
@Data
public class TracingConfig {

    /**
     * 事件追踪 类型
     */
    private String type;

    /**
     * 数据源bean名称
     */
    private String dataSourceBeanName;
}
