package com.github.kangarooxin.spring.boot.starter.elastic.job3.annotation;

import com.github.kangarooxin.spring.boot.starter.elastic.job3.constant.JobErrorHandlerType;
import com.github.kangarooxin.spring.boot.starter.elastic.job3.constant.JobExecutorServiceHandlerType;
import com.github.kangarooxin.spring.boot.starter.elastic.job3.constant.JobShardingStrategyType;
import com.github.kangarooxin.spring.boot.starter.elastic.job3.constant.JobType;

import java.lang.annotation.*;

/**
 * @author kangaroo_xin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@Repeatable(ElasticJobMultiScheduler.class)
public @interface ElasticJobScheduler {

    /**
     * 作业类型
     *
     * @return
     */
    JobType jobType() default JobType.AUTO;

    /**
     * 任务名称
     * <p>
     * 默认名称：beanName + ElasticJob
     *
     * @return
     */
    String name() default "";

    /**
     * cron表达式，用于控制作业触发时间
     * <p>
     * 如果未设置，会取配置中根据jobName设置的cron
     * 如果都未设置，会生成一次性调度
     *
     * @return
     */
    String cron() default "";

    /**
     * 总分片数
     *
     * @return
     */
    int shardingTotalCount() default 1;

    /**
     * 分片参数
     *
     * @return
     */
    String shardingItemParameters() default "";

    /**
     * 任务描述
     *
     * @return
     */
    String description() default "";

    /**
     * 任务自定义参数
     */
    String jobParameters() default "";

    /**
     * 一次性调度Bean名称
     * <p>
     * 默认格式： jobName + OneOffJobBootstrap
     *
     * @return
     */
    String jobBootstrapBeanName() default "";

    /**
     * 配置错误处理策略
     * {@link org.apache.shardingsphere.elasticjob.error.handler.JobErrorHandler}
     * <p>
     * 官方支持类型
     * {@link JobErrorHandlerType}
     *
     * @return
     */
    String jobErrorHandlerType() default "";

    /**
     * 任务监听器
     * <p>
     * 常规监听器 {@link org.apache.shardingsphere.elasticjob.infra.listener.ElasticJobListener}
     * <p>
     * 分布式监听器 {@link org.apache.shardingsphere.elasticjob.lite.api.listener.AbstractDistributeOnceElasticJobListener}
     * <p>
     * 添加SPI实现
     * 将JobListener实现添加至infra-common下resources/META-INF/services/org.apache.shardingsphere.elasticjob.infra.listener.ElasticJobListener
     *
     * @return
     */
    String[] jobListenerTypes() default {};

    /**
     * Set enable or disable monitor execution.
     *
     * <p>
     * For short interval job, it is better to disable monitor execution to improve performance.
     * It can't guarantee repeated data fetch and can't failover if disable monitor execution, please keep idempotence in job.
     * <p>
     * For long interval job, it is better to enable monitor execution to guarantee fetch data exactly once.
     * </p>
     *
     * @return
     */
    boolean monitorExecution() default false;

    /**
     * Set enable failover.
     *
     * <p>
     * Only for `monitorExecution` enabled.
     * </p>
     *
     * @return
     */
    boolean failover() default false;

    /**
     * Set enable misfire.
     *
     * @return
     */
    boolean misfire() default false;

    /**
     * Set max tolerate time different seconds between job server and registry center.
     *
     * <p>
     * ElasticJob will throw exception if exceed max tolerate time different seconds.
     * -1 means do not check.
     * </p>
     *
     * @return
     */
    int maxTimeDiffSeconds() default -1;

    /**
     * Set reconcile interval minutes for job sharding status.
     *
     * <p>
     * Monitor the status of the job server at regular intervals, and resharding if incorrect.
     * </p>
     *
     * @return
     */
    int reconcileIntervalMinutes() default 0;

    /**
     * Set job sharding strategy type.
     *
     * <p>
     * Default for {@code AverageAllocationJobShardingStrategy}.
     * <p>
     * {@link org.apache.shardingsphere.elasticjob.infra.handler.sharding.JobShardingStrategy}
     * <p>
     * {@link JobShardingStrategyType}
     * <p>
     * AVG_ALLOCATION
     * ODEVITY
     * ROUND_ROBIN
     *
     * </p>
     *
     * @return
     */
    String jobShardingStrategyType() default "";

    /**
     * Set job executor service handler type.
     * <p>
     * {@link org.apache.shardingsphere.elasticjob.infra.handler.threadpool.JobExecutorServiceHandler}
     * <p>
     * {@link JobExecutorServiceHandlerType}
     * <p>
     * CPU
     * SINGLE_THREAD
     *
     * @return
     */
    String jobExecutorServiceHandlerType() default "";

    /**
     * Set whether disable job when start.
     *
     * <p>
     * Using in job deploy, start job together after deploy.
     * </p>
     *
     * @return
     */
    boolean disabled() default false;

    /**
     * Set whether overwrite local configuration to registry center when job startup.
     *
     * <p>
     * If overwrite enabled, every startup will use local configuration.
     * </p>
     *
     * @return
     */
    boolean overwrite() default true;

    /**
     * 是否开启流式处理
     * <p>
     * 仅数据流作业时有效
     *
     * @return
     */
    boolean streamingProcess() default false;

    /**
     * 脚本内容或运行路径
     * <p>
     * 仅脚本作业时有效
     *
     * @return
     */
    String scriptCommandLine() default "";

    /**
     * http请求url
     *
     * @return
     */
    String httpUrl() default "";

    /**
     * http请求方法
     *
     * @return
     */
    String httpMethod() default "POST";

    /**
     * http请求数据
     *
     * @return
     */
    String httpData() default "";

    /**
     * http连接超时
     *
     * @return
     */
    long httpConnectTimeoutMilliseconds() default 3000;

    /**
     * http读超时
     *
     * @return
     */
    long httpReadTimeoutMilliseconds() default 5000;

    /**
     * http请求ContentType
     *
     * @return
     */
    String httpContentType() default "";

}
