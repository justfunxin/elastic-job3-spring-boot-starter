package com.github.kangarooxin.spring.boot.starter.elastic.job3.service;

import com.github.kangarooxin.spring.boot.starter.elastic.job3.model.HttpJobProp;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.JobBootstrap;

/**
 * @author kangaroo_xin
 */
public interface ElasticJobService {

    /**
     * 添加简单作业
     *
     * @param elasticJob
     * @param jobName
     * @param shardingTotalCount
     * @param cron
     */
    JobBootstrap addJob(ElasticJob elasticJob, String jobName, int shardingTotalCount, String cron);

    /**
     * 添加简单作业
     *
     * @param elasticJob
     * @param jobName
     * @param shardingTotalCount
     * @param cron
     * @param jobParameter
     * @param shardingItemParameters
     */
    JobBootstrap addJob(ElasticJob elasticJob, String jobName, int shardingTotalCount, String cron, String jobParameter, String... shardingItemParameters);

    /**
     * 添加简单作业
     *
     * @param jobConfiguration
     * @param jobBootstrapBeanName 非必填
     * @param elasticJob
     * @return
     */
    JobBootstrap addJob(JobConfiguration jobConfiguration, String jobBootstrapBeanName, ElasticJob elasticJob);

    /**
     * 添加数据流作业
     *
     * @param jobConfiguration
     * @param jobBootstrapBeanName
     * @param elasticJob
     * @param streamingProcess     是否开启流式处理 默认false
     * @return
     */
    <T> JobBootstrap addDataFlowJob(JobConfiguration jobConfiguration, String jobBootstrapBeanName, DataflowJob<T> elasticJob, boolean streamingProcess);

    /**
     * 添加定时Script调度
     *
     * @param jobConfiguration
     * @param scriptCommendLine
     */
    JobBootstrap addScriptJob(JobConfiguration jobConfiguration, String jobBootstrapBeanName, String scriptCommendLine);

    /**
     * 添加定时Http调度
     *
     * @param jobConfiguration
     * @param httpProp
     */
    JobBootstrap addHttpJob(JobConfiguration jobConfiguration, String jobBootstrapBeanName, HttpJobProp httpProp);

}
