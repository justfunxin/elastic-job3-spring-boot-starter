# elastic-job3-spring-boot-starter

elastic job3 spring boot starter

## Usage:
1. build and install jar
```
git clone https://github.com/kangarooxin/elastic-job3-spring-boot-starter.git
cd elastic-job3-spring-boot-starter
mvc clean install
```
2. import dependency in pom.xml
```
<dependency>
    <groupId>com.github.kangarooxin</groupId>
    <artifactId>elastic-job3-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
3. config in properties
```properties
   #reg-center
   elasticjob.reg-center.server-lists=localhost:2181
   elasticjob.reg-center.namespace=${spring.application.name}.elastic-job
   #tracing
   #elasticjob.tracing.type=RDB
   #multiple datasource
   #elasticjob.tracing.data-source-bean-name=db1MasterSlaveRoutingDatasource
```
4. use annotation
```java
@Service
@ElasticJobScheduler(cron = "0/5 * * * * ?", shardingTotalCount = 4, name = "SimpleElasticJob",
        shardingItemParameters = "0=0,1=0,2=1,3=1", jobParameters = "parameter")
@Slf4j
public class SimpleElasticJob implements SimpleJob {

   @Override
   public void execute(ShardingContext shardingContext) {
      log.info("Thread ID: {}, ShardingTotalCount: {}, ShardingItem: {}, ShardingParameter: {}, JobName: {}, JobParameter: {}",
              Thread.currentThread().getId(),
              shardingContext.getShardingTotalCount(),
              shardingContext.getShardingItem(),
              shardingContext.getShardingParameter(),
              shardingContext.getJobName(),
              shardingContext.getJobParameter()
      );
   }
}
```
5. multiple job
```java
@Service
@ElasticJobScheduler(cron = "0/5 * * * * ?", shardingTotalCount = 4, name = "SimpleElasticJobLevel1",
        shardingItemParameters = "0=0,1=0,2=1,3=1", jobParameters = "parameter")
@ElasticJobScheduler(cron = "0 */1 * * * ?", shardingTotalCount = 4, name = "SimpleElasticJobLevel2",
        shardingItemParameters = "0=0,1=0,2=1,3=1", jobParameters = "parameter")
@Slf4j
public class MultipleElasticJob implements SimpleJob {

   @Override
   public void execute(ShardingContext shardingContext) {
      log.info("Thread ID: {}, ShardingTotalCount: {}, ShardingItem: {}, ShardingParameter: {}, JobName: {}, JobParameter: {}",
              Thread.currentThread().getId(),
              shardingContext.getShardingTotalCount(),
              shardingContext.getShardingItem(),
              shardingContext.getShardingParameter(),
              shardingContext.getJobName(),
              shardingContext.getJobParameter()
      );
   }
}
```