package com.github.justfunxin.spring.boot.starter.elastic.job3.service;

import com.github.justfunxin.spring.boot.starter.elastic.job3.model.HttpJobProp;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.apache.shardingsphere.elasticjob.infra.pojo.JobConfigurationPOJO;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.JobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.lifecycle.domain.JobBriefInfo;

import java.util.Collection;

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

    /**
     * remove job
     *
     * @param jobName
     */
    void removeJob(String jobName);

    /**
     * remove job
     *
     * @param jobName
     * @param jobBootstrapBeanName
     */
    void removeJob(String jobName, String jobBootstrapBeanName);

    /**
     * get job configuration.
     *
     * @param jobName job name
     * @return job configuration
     */
    JobConfigurationPOJO getJobConfiguration(String jobName);

    /**
     * Update job configuration.
     *
     * @param jobConfig job configuration
     */
    void updateJobConfiguration(JobConfigurationPOJO jobConfig);

    /**
     * Remove job configuration.
     *
     * @param jobName job name
     */
    void removeJobConfiguration(String jobName);

    /**
     * Trigger job to run at once.
     *
     * <p>Job will not start until it does not conflict with the last running job, and this tag will be automatically cleaned up after it starts.</p>
     *
     * @param jobName job name
     */
    void trigger(String jobName);

    /**
     * Get jobs total count.
     *
     * @return jobs total count.
     */
    int getJobsTotalCount();

    /**
     * Get all jobs brief info.
     *
     * @return all jobs brief info.
     */
    Collection<JobBriefInfo> getAllJobsBriefInfo();

    /**
     * Get job brief info.
     *
     * @param jobName job name
     * @return job brief info
     */
    JobBriefInfo getJobBriefInfo(String jobName);
}
