package com.github.kangarooxin.spring.boot.starter.elastic.job3.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author kangaroo_xin
 */
@Data
@Accessors(chain = true)
public class HttpJobProp {

    /**
     * http请求url
     */
    private String url;

    /**
     * http请求方法
     */
    private String method;

    /**
     * http请求数据
     */
    private String data;

    /**
     * http连接超时，默认3000
     */
    private Long connectTimeoutMilliseconds;

    /**
     * http读超时，默认5000
     */
    private Long readTimeoutMilliseconds;

    /**
     * http请求ContentType
     */
    private String contentType;
}
