package com.github.kangarooxin.spring.boot.starter.elastic.job3.service.impl;

import com.github.kangarooxin.spring.boot.starter.elastic.job3.constant.JobType;
import com.github.kangarooxin.spring.boot.starter.elastic.job3.model.HttpJobProp;
import com.github.kangarooxin.spring.boot.starter.elastic.job3.properties.ElasticJobSchedulerProperties;
import com.github.kangarooxin.spring.boot.starter.elastic.job3.service.ElasticJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.JobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.OneOffJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author kangaroo_xin
 */
@Slf4j
public class ElasticJobServiceImpl implements ElasticJobService, ApplicationContextAware {

    private static final String JOB_BOOTSTRAP_BEAN_NAME = "JobBootstrap";

    private CoordinatorRegistryCenter elasticJobRegCenter;

    private ElasticJobSchedulerProperties properties;

    private DefaultListableBeanFactory beanFactory;

    public ElasticJobServiceImpl(CoordinatorRegistryCenter elasticJobRegCenter, ElasticJobSchedulerProperties properties) {
        this.properties = properties;
        this.elasticJobRegCenter = elasticJobRegCenter;
    }

    @Override
    public JobBootstrap addJob(ElasticJob elasticJob, String jobName, int shardingTotalCount, String cron) {
        JobConfiguration jobConfiguration = JobConfiguration.newBuilder(jobName, shardingTotalCount)
                .cron(cron)
                .overwrite(true)
                .build();
        return addJob(jobConfiguration, null, elasticJob);
    }

    @Override
    public JobBootstrap addJob(ElasticJob elasticJob, String jobName, int shardingTotalCount, String cron,
                               String jobParameter, String... shardingItemParameters) {
        JobConfiguration jobConfiguration = JobConfiguration.newBuilder(jobName, shardingTotalCount)
                .cron(cron)
                .shardingItemParameters(StringUtils.arrayToCommaDelimitedString(shardingItemParameters))
                .jobParameter(jobParameter)
                .overwrite(true)
                .build();
        return addJob(jobConfiguration, null, elasticJob);
    }

    @Override
    public JobBootstrap addJob(JobConfiguration jobConfiguration, String jobBootstrapBeanName, ElasticJob elasticJob) {
        jobConfiguration = buildJobConfiguration(jobConfiguration);
        if (StringUtils.isEmpty(jobBootstrapBeanName)) {
            jobBootstrapBeanName = jobConfiguration.getJobName() + JOB_BOOTSTRAP_BEAN_NAME;
        }
        if (StringUtils.isEmpty(jobConfiguration.getCron())) {
            OneOffJobBootstrap jobBootstrap = new OneOffJobBootstrap(elasticJobRegCenter, elasticJob, jobConfiguration);
            registerBean(jobBootstrapBeanName, jobBootstrap);
            return jobBootstrap;
        } else {
            ScheduleJobBootstrap jobBootstrap = new ScheduleJobBootstrap(elasticJobRegCenter, elasticJob, jobConfiguration);
            jobBootstrap.schedule();
            registerBean(jobBootstrapBeanName, jobBootstrap);
            return jobBootstrap;
        }
    }

    private void registerBean(String jobBootstrapBeanName, JobBootstrap jobBootstrap) {
        if (beanFactory.containsBean(jobBootstrapBeanName)) {
            Object object = beanFactory.getBean(jobBootstrapBeanName);
            if (object instanceof JobBootstrap) {
                log.info("JobBootstrap bean `{}` existed", jobBootstrapBeanName);
            } else {
                throw new IllegalStateException("JobBootstrap bean name existed and type is not match, jobBootstrapBeanName=" + jobBootstrapBeanName);
            }
        } else {
            beanFactory.registerSingleton(jobBootstrapBeanName, jobBootstrap);
        }
    }

    @Override
    public <T> JobBootstrap addDataFlowJob(JobConfiguration jobConfiguration, String jobBootstrapBeanName, DataflowJob<T> elasticJob, boolean streamingProcess) {
        jobConfiguration.getProps().setProperty("streaming.process", String.valueOf(streamingProcess));
        return addJob(jobConfiguration, jobBootstrapBeanName, elasticJob);
    }

    @Override
    public JobBootstrap addScriptJob(JobConfiguration jobConfiguration, String jobBootstrapBeanName, String scriptCommendLine) {
        jobConfiguration = buildJobConfiguration(jobConfiguration);
        if (StringUtils.isEmpty(jobBootstrapBeanName)) {
            jobBootstrapBeanName = jobConfiguration.getJobName() + JOB_BOOTSTRAP_BEAN_NAME;
        }
        jobConfiguration.getProps().setProperty("script.command.line", scriptCommendLine);
        if (StringUtils.isEmpty(jobConfiguration.getCron())) {
            OneOffJobBootstrap jobBootstrap = new OneOffJobBootstrap(elasticJobRegCenter, JobType.SCRIPT.name(), jobConfiguration);
            beanFactory.registerSingleton(jobBootstrapBeanName, jobBootstrap);
            return jobBootstrap;
        } else {
            ScheduleJobBootstrap jobBootstrap = new ScheduleJobBootstrap(elasticJobRegCenter, JobType.SCRIPT.name(), jobConfiguration);
            jobBootstrap.schedule();
            beanFactory.registerSingleton(jobBootstrapBeanName, jobBootstrap);
            return jobBootstrap;
        }
    }

    @Override
    public JobBootstrap addHttpJob(JobConfiguration jobConfiguration, String jobBootstrapBeanName, HttpJobProp httpProp) {
        jobConfiguration = buildJobConfiguration(jobConfiguration);
        if (StringUtils.isEmpty(jobBootstrapBeanName)) {
            jobBootstrapBeanName = jobConfiguration.getJobName() + JOB_BOOTSTRAP_BEAN_NAME;
        }
        Properties jobProp = jobConfiguration.getProps();
        jobProp.setProperty("http.url", httpProp.getUrl());
        if (StringUtils.hasText(httpProp.getMethod())) {
            jobProp.setProperty("http.method", httpProp.getMethod());
        }
        if (StringUtils.hasText(httpProp.getData())) {
            jobProp.setProperty("http.data", httpProp.getData());
        }
        if (httpProp.getConnectTimeoutMilliseconds() > 0) {
            jobProp.setProperty("http.connect.timeout.milliseconds", String.valueOf(httpProp.getConnectTimeoutMilliseconds()));
        }
        if (httpProp.getReadTimeoutMilliseconds() > 0) {
            jobProp.setProperty("http.read.timeout.milliseconds", String.valueOf(httpProp.getReadTimeoutMilliseconds()));
        }
        if (StringUtils.hasText(httpProp.getContentType())) {
            jobProp.setProperty("http.content.type", httpProp.getContentType());
        }
        if (StringUtils.isEmpty(jobConfiguration.getCron())) {
            OneOffJobBootstrap jobBootstrap = new OneOffJobBootstrap(elasticJobRegCenter, JobType.HTTP.name(), jobConfiguration);
            beanFactory.registerSingleton(jobBootstrapBeanName, jobBootstrap);
            return jobBootstrap;
        } else {
            ScheduleJobBootstrap jobBootstrap = new ScheduleJobBootstrap(elasticJobRegCenter, JobType.HTTP.name(), jobConfiguration);
            jobBootstrap.schedule();
            beanFactory.registerSingleton(jobBootstrapBeanName, jobBootstrap);
            return jobBootstrap;
        }
    }

    private JobConfiguration buildJobConfiguration(JobConfiguration jobConfiguration) {
        JobConfiguration.Builder builder = JobConfiguration.newBuilder(jobConfiguration.getJobName(), jobConfiguration.getShardingTotalCount())
                .cron(jobConfiguration.getCron())
                .shardingItemParameters(jobConfiguration.getShardingItemParameters())
                .jobParameter(jobConfiguration.getJobParameter())
                .monitorExecution(jobConfiguration.isMonitorExecution())
                .failover(jobConfiguration.isFailover())
                .misfire(jobConfiguration.isMisfire())
                .maxTimeDiffSeconds(jobConfiguration.getMaxTimeDiffSeconds())
                .reconcileIntervalMinutes(jobConfiguration.getReconcileIntervalMinutes())
                .jobShardingStrategyType(StringUtils.hasText(jobConfiguration.getJobShardingStrategyType()) ? jobConfiguration.getJobShardingStrategyType() : properties.getJobShardingStrategyType())
                .jobExecutorServiceHandlerType(StringUtils.hasText(jobConfiguration.getJobExecutorServiceHandlerType()) ? jobConfiguration.getJobExecutorServiceHandlerType() : properties.getJobExecutorServiceHandlerType())
                .jobErrorHandlerType(StringUtils.hasText(jobConfiguration.getJobErrorHandlerType()) ? jobConfiguration.getJobErrorHandlerType() : properties.getJobErrorHandlerType())
                .description(jobConfiguration.getDescription())
                .disabled(jobConfiguration.isDisabled())
                .overwrite(jobConfiguration.isOverwrite());
        if (jobConfiguration.getJobListenerTypes() != null) {
            builder.jobListenerTypes(jobConfiguration.getJobListenerTypes().toArray(new String[0]));
        }
        if (jobConfiguration.getExtraConfigurations() != null) {
            jobConfiguration.getExtraConfigurations().forEach(builder::addExtraConfigurations);
        }
        Properties configProps = properties.getProps();
        if (configProps != null) {
            configProps.stringPropertyNames().forEach(each -> builder.setProperty(each, configProps.getProperty(each)));
        }
        Properties jobProps = jobConfiguration.getProps();
        if (jobProps != null) {
            jobProps.stringPropertyNames().forEach(each -> builder.setProperty(each, jobProps.getProperty(each)));
        }
        return builder.build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
    }
}
