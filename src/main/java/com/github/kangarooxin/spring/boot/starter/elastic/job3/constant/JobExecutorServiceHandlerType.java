package com.github.kangarooxin.spring.boot.starter.elastic.job3.constant;

/**
 * 线程池策略
 *
 * {@link org.apache.shardingsphere.elasticjob.infra.handler.threadpool.JobExecutorServiceHandler}
 *
 * @author kangaroo_xin
 */
public enum JobExecutorServiceHandlerType {

    /**
     * CPU 资源策略
     * {@link org.apache.shardingsphere.elasticjob.infra.handler.threadpool.impl.CPUUsageJobExecutorServiceHandler}
     *
     * 根据 CPU 核数 * 2 创建作业处理线程池。
     */
    CPU,

    /**
     * 单线程策略
     * {@link org.apache.shardingsphere.elasticjob.infra.handler.threadpool.impl.SingleThreadJobExecutorServiceHandler}
     *
     * 使用单线程处理作业。
     */
    SINGLE_THREAD;
}
