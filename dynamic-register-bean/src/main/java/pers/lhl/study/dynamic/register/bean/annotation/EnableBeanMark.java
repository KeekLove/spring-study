package pers.lhl.study.dynamic.register.bean.annotation;

import org.springframework.context.annotation.Import;
import pers.lhl.study.dynamic.register.bean.register.BeanMarkRegister;

import java.lang.annotation.*;
/**
 * @author LHL
 * @version 1.0
 * @Description {@link BeanMark}¿ª¹Ø
 * @date 2022/3/1 21:55
 * @since
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BeanMarkRegister.class)
public @interface EnableBeanMark {
    String[] packages()  default "";
}
