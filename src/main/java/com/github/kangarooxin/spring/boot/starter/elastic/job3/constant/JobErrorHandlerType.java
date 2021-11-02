package com.github.kangarooxin.spring.boot.starter.elastic.job3.constant;

/**
 * 错误处理策略
 *
 * {@link org.apache.shardingsphere.elasticjob.error.handler.JobErrorHandler}
 *
 * @author kangaroo_xin
 */
public enum JobErrorHandlerType {
    /**
     * 记录日志策略
     * 记录作业异常日志，但不中断作业执行
     * {@link org.apache.shardingsphere.elasticjob.error.handler.general.LogJobErrorHandler}
     *
     */
    LOG,

    /**
     * 抛出异常策略
     * 抛出系统异常并中断作业执行
     * {@link org.apache.shardingsphere.elasticjob.error.handler.general.ThrowJobErrorHandler}
     *
     */
    THROW,

    /**
     * 忽略异常策略
     * 忽略系统异常且不中断作业执行
     * {@link org.apache.shardingsphere.elasticjob.error.handler.general.IgnoreJobErrorHandler}
     *
     */
    IGNORE,

    /**
     * 邮件通知策略
     * 发送邮件消息通知，但不中断作业执行
     *
     * elasticjob-error-handler-email
     *
     * 属性名	        说明	            是否必填	默认值
     * email.host	    邮件服务器地址	    是	    -
     * email.port	    邮件服务器端口	    是	    -
     * email.username	邮件服务器用户名	    是	    -
     * email.password	邮件服务器密码	    是	    -
     * email.useSsl	    是否启用 SSL 加密传输	否	    true
     * email.subject	邮件主题	            否	    ElasticJob error message
     * email.from	    发送方邮箱地址	    是	    -
     * email.to	        接收方邮箱地址	    是	    -
     * email.cc	        抄送邮箱地址	        否	    null
     * email.bcc	    密送邮箱地址	        否	    null
     * email.debug	    是否开启调试模式	    否	    false
     */
    EMAIL,

    /**
     * 企业微信通知策略
     * 发送企业微信消息通知，但不中断作业执行
     *
     * elasticjob-error-handler-wechat
     *
     * 属性名	                            说明	                            是否必填	默认值
     * wechat.webhook	                    企业微信机器人的 webhook 地址	        是	    -
     * wechat.connectTimeoutMilliseconds	与企业微信服务器建立连接的超时时间	    否	    3000 毫秒
     * wechat.readTimeoutMilliseconds	    从企业微信服务器读取到可用资源的超时时间	否	    5000 毫秒
     */
    WECHAT,

    /**
     * 钉钉通知策略
     * 发送钉钉消息通知，但不中断作业执行
     *
     * elasticjob-error-handler-dingtalk
     *
     * 属性名	                            说明	                                是否必填	默认值
     * dingtalk.webhook	                    钉钉机器人的 webhook 地址	                是	    -
     * dingtalk.keyword	                    自定义关键词	                            否	    null
     * dingtalk.secret	                    签名的密钥	                            否	    null
     * dingtalk.connectTimeoutMilliseconds	与钉钉服务器建立连接的超时时间	            否	    3000 毫秒
     * dingtalk.readTimeoutMilliseconds	    从钉钉服务器读取到可用资源的超时时间         否	    5000 毫秒
     */
    DINGTALK;
}
