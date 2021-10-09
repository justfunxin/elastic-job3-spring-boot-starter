package com.github.kangarooxin.spring.boot.starter.elastic.job3.annotation;

import java.lang.annotation.*;

/**
 * @author kangaroo_xin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface ElasticJobMultiScheduler {

    /**
     *
     * @return
     */
    ElasticJobScheduler[] value() default {};
}
