package com.github.kangarooxin.spring.boot.starter.elastic.job3.properties;

import com.github.kangarooxin.spring.boot.starter.elastic.job3.constant.Constants;
import com.github.kangarooxin.spring.boot.starter.elastic.job3.constant.JobErrorHandlerType;
import com.github.kangarooxin.spring.boot.starter.elastic.job3.constant.JobExecutorServiceHandlerType;
import com.github.kangarooxin.spring.boot.starter.elastic.job3.constant.JobShardingStrategyType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Properties;

/**
 * @author kangaroo_xin
 */
@Data
@ConfigurationProperties(prefix = Constants.ELASTIC_JOB_PREFIX)
public class ElasticJobSchedulerProperties {

    private boolean enabled = true;

    /**
     * 默认错误处理策略
     *
     */
    private String jobErrorHandlerType = JobErrorHandlerType.LOG.name();

    /**
     * 默认分片策略
     */
    private String jobShardingStrategyType = JobShardingStrategyType.AVG_ALLOCATION.name();

    /**
     * 默认线程策略
     */
    private String jobExecutorServiceHandlerType = JobExecutorServiceHandlerType.CPU.name();

    /**
     * 默认错误处理策略
     *
     * <pre>
     * props:
     *     email:
     *       host: host
     *       port: 465
     *       username: username
     *       password: password
     *       useSsl: true
     *       subject: ElasticJob error message
     *       from: from@xxx.xx
     *       to: to1@xxx.xx,to2@xxx.xx
     *       cc: cc@xxx.xx
     *       bcc: bcc@xxx.xx
     *       debug: false
     *     wechat:
     *       webhook: you_webhook
     *       connectTimeout: 3000
     *       readTimeout: 5000
     *     dingtalk:
     *        webhook: you_webhook
     *        keyword: you_keyword
     *        secret: you_secret
     *        connectTimeout: 3000
     *        readTimeout: 5000
     * </pre>
     */
    private Properties props = new Properties();

    /**
     * configuring network, preferred ip
     *
     */
    @NestedConfigurationProperty
    private Network network = new Network();

    /**
     *
     */
    @NestedConfigurationProperty
    private TracingConfig tracing = new TracingConfig();


    @Data
    public static class Network {
        /**
         * configure preferred interface by regex
         *
         * elasticjob.preferred.network.interface
         */
        private String preferredInterface;

        /**
         * configure preferred ip by regex
         *
         * elasticjob.preferred.network.ip
         *
         */
        private String preferredIp;
    }
}
