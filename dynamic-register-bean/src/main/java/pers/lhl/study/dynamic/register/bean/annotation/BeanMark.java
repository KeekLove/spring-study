package pers.lhl.study.dynamic.register.bean.annotation;


import java.lang.annotation.*;


/**
 * @author LHL
 * @version 1.0
 * @Description 注解标记，被此注解标记将被扫描成bean
 * @date 2022/3/1 21:55
 * @since
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BeanMark {
}
