package com.github.kangarooxin.spring.boot.starter.elastic.job3.constant;

/**
 * 作业类型.
 *
 * @author kangaroo_xin
 */
public enum JobType {

    /**
     * 根据bean类型自动判断是简单作业还是数据流作业
     *
     */
    AUTO,

    /**
     * 简单作业
     */
    SIMPLE,

    /**
     * 数据流作业
     *
     * 名称	                数据类型	说明	        默认值
     * streaming.process	boolean	是否开启流式处理	false
     */
    DATAFLOW,

    /**
     * 脚本作业
     *
     * 名称	                数据类型	说明	            默认值
     * script.command.line	String	脚本内容或运行路径	-
     */
    SCRIPT,

    /**
     * HTTP作业
     *
     * 名称	                                数据类型	说明	            默认值
     * http.url	                            String	http请求url	        -
     * http.method	                        String	http请求方法        	-
     * http.data	                        String	http请求数据	        -
     * http.connect.timeout.milliseconds	String	http连接超时	        3000
     * http.read.timeout.milliseconds	    String	http读超时	        5000
     * http.content.type	                String	http请求ContentType	-
     */
    HTTP
}
