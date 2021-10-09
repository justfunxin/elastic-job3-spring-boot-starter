package com.github.kangarooxin.spring.boot.starter.elastic.job3.constant;

/**
 * 作业分片策略
 *
 * {@link org.apache.shardingsphere.elasticjob.infra.handler.sharding.JobShardingStrategy}
 *
 * @author kangaroo_xin
 */
public enum JobShardingStrategyType {
    /**
     * 平均分片策略
     * {@link org.apache.shardingsphere.elasticjob.infra.handler.sharding.impl.AverageAllocationJobShardingStrategy}
     *
     * 根据分片项平均分片。
     *
     * 如果作业服务器数量与分片总数无法整除，多余的分片将会顺序的分配至每一个作业服务器。
     *
     * 举例说明：
     *
     * 如果 3 台作业服务器且分片总数为9，则分片结果为：1=[0,1,2], 2=[3,4,5], 3=[6,7,8]；
     * 如果 3 台作业服务器且分片总数为8，则分片结果为：1=[0,1,6], 2=[2,3,7], 3=[4,5]；
     * 如果 3 台作业服务器且分片总数为10，则分片结果为：1=[0,1,2,9], 2=[3,4,5], 3=[6,7,8]。
     */
    AVG_ALLOCATION,

    /**
     * 奇偶分片策略
     * {@link org.apache.shardingsphere.elasticjob.infra.handler.sharding.impl.OdevitySortByNameJobShardingStrategy}
     *
     * 根据作业名称哈希值的奇偶数决定按照作业服务器 IP 升序或是降序的方式分片。
     *
     * 如果作业名称哈希值是偶数，则按照 IP 地址进行升序分片； 如果作业名称哈希值是奇数，则按照 IP 地址进行降序分片。
     * 可用于让服务器负载在多个作业共同运行时分配的更加均匀。
     *
     * 举例说明：
     *
     * 如果 3 台作业服务器，分片总数为2且作业名称的哈希值为偶数，则分片结果为：1 = [0], 2 = [1], 3 = []；
     * 如果 3 台作业服务器，分片总数为2且作业名称的哈希值为奇数，则分片结果为：3 = [0], 2 = [1], 1 = []。
     */
    ODEVITY,

    /**
     * 轮询分片策略
     * {@link org.apache.shardingsphere.elasticjob.infra.handler.sharding.impl.RoundRobinByNameJobShardingStrategy}
     *
     * 根据作业名称轮询分片。
     */
    ROUND_ROBIN;
}
