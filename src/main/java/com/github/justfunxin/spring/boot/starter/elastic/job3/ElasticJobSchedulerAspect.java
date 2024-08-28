package com.github.justfunxin.spring.boot.starter.elastic.job3;

import com.github.justfunxin.spring.boot.starter.elastic.job3.annotation.ElasticJobMultiScheduler;
import com.github.justfunxin.spring.boot.starter.elastic.job3.annotation.ElasticJobScheduler;
import com.github.justfunxin.spring.boot.starter.elastic.job3.model.HttpJobProp;
import com.github.justfunxin.spring.boot.starter.elastic.job3.properties.ElasticJobSchedulerProperties;
import com.github.justfunxin.spring.boot.starter.elastic.job3.service.ElasticJobService;
import jakarta.annotation.PostConstruct;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @author kangaroo_xin
 */
@Component
public class ElasticJobSchedulerAspect implements ApplicationContextAware {

    private static final String ELASTIC_JOB_NAME = "ElasticJob";

    private ApplicationContext applicationContext;

    @Autowired
    private ElasticJobService elasticJobService;

    @Autowired
    private ElasticJobSchedulerProperties elasticJobSchedulerProperties;

    /**
     * 解析context信息，开始注册
     */
    @PostConstruct
    public void registerJob() {
        registerSimpleJob();
        registerMultiJob();
    }

    private void registerSimpleJob() {
        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(ElasticJobScheduler.class);
        for (String beanName : beanNamesForAnnotation) {
            Class<?> handlerType = applicationContext.getType(beanName);
            Object bean = applicationContext.getBean(beanName);
            if (!(bean instanceof ElasticJob)) {
                continue;
            }
            ElasticJobMultiScheduler multiAnnotation = AnnotationUtils.findAnnotation(handlerType, ElasticJobMultiScheduler.class);
            if (multiAnnotation != null) {
                ElasticJobScheduler[] values = multiAnnotation.value();
                for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
                    addJobToContext(values[i], (ElasticJob) bean, beanName + ELASTIC_JOB_NAME + i);
                }
            } else {
                ElasticJobScheduler annotation = AnnotationUtils.findAnnotation(handlerType, ElasticJobScheduler.class);
                addJobToContext(annotation, (ElasticJob) bean, beanName + ELASTIC_JOB_NAME);
            }
        }
    }

    private void registerMultiJob() {
        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(ElasticJobMultiScheduler.class);
        for (String beanName : beanNamesForAnnotation) {
            Class<?> handlerType = applicationContext.getType(beanName);
            Object bean = applicationContext.getBean(beanName);
            if (!(bean instanceof ElasticJob)) {
                continue;
            }
            ElasticJobMultiScheduler annotation = AnnotationUtils.findAnnotation(handlerType, ElasticJobMultiScheduler.class);
            if (annotation == null) {
                continue;
            }
            ElasticJobScheduler[] values = annotation.value();
            for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
                addJobToContext(values[i], (ElasticJob) bean, beanName + ELASTIC_JOB_NAME + i);
            }
        }
    }

    /**
     * 将任务添加到容器中
     *
     * @param elasticScheduler
     * @param elasticJob
     */
    private void addJobToContext(ElasticJobScheduler elasticScheduler, ElasticJob elasticJob, String jobName) {
        if (StringUtils.hasText(elasticScheduler.name())) {
            jobName = elasticScheduler.name();
        }
        String cron = elasticScheduler.cron();
        if (StringUtils.isEmpty(cron)) {
            cron = elasticJobSchedulerProperties.getCrons().get(jobName);
        }
        JobConfiguration jobConfiguration = JobConfiguration.newBuilder(jobName, elasticScheduler.shardingTotalCount())
                .cron(cron)
                .shardingItemParameters(elasticScheduler.shardingItemParameters())
                .jobParameter(elasticScheduler.jobParameters())
                .monitorExecution(elasticScheduler.monitorExecution())
                .failover(elasticScheduler.failover())
                .misfire(elasticScheduler.misfire())
                .maxTimeDiffSeconds(elasticScheduler.maxTimeDiffSeconds())
                .reconcileIntervalMinutes(elasticScheduler.reconcileIntervalMinutes())
                .jobShardingStrategyType(elasticScheduler.jobShardingStrategyType())
                .jobExecutorServiceHandlerType(elasticScheduler.jobExecutorServiceHandlerType())
                .description(elasticScheduler.description())
                .jobListenerTypes(elasticScheduler.jobListenerTypes())
                .jobErrorHandlerType(elasticScheduler.jobErrorHandlerType())
                .disabled(elasticScheduler.disabled())
                .overwrite(elasticScheduler.overwrite())
                .build();
        addScheduleJob(elasticScheduler.jobBootstrapBeanName(), elasticScheduler, elasticJob, jobConfiguration);
    }

    private void addScheduleJob(String jobBootstrapBeanName, ElasticJobScheduler elasticScheduler, ElasticJob elasticJob, JobConfiguration jobConfiguration) {
        switch (elasticScheduler.jobType()) {
            case SCRIPT:
                elasticJobService.addScriptJob(jobConfiguration, jobBootstrapBeanName, elasticScheduler.scriptCommandLine());
                break;
            case HTTP:
                HttpJobProp httpJobProp = new HttpJobProp()
                        .setUrl(elasticScheduler.httpUrl())
                        .setMethod(elasticScheduler.httpMethod())
                        .setData(elasticScheduler.httpData())
                        .setConnectTimeoutMilliseconds(elasticScheduler.httpConnectTimeoutMilliseconds())
                        .setReadTimeoutMilliseconds(elasticScheduler.httpReadTimeoutMilliseconds())
                        .setContentType(elasticScheduler.httpContentType());
                elasticJobService.addHttpJob(jobConfiguration, jobBootstrapBeanName, httpJobProp);
                break;
            default:
                if (elasticJob instanceof DataflowJob) {
                    elasticJobService.addDataFlowJob(jobConfiguration, jobBootstrapBeanName, (DataflowJob<?>) elasticJob, elasticScheduler.streamingProcess());
                } else {
                    elasticJobService.addJob(jobConfiguration, jobBootstrapBeanName, elasticJob);
                }
                break;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
