package pers.lhl.study.dynamic.register.bean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pers.lhl.study.dynamic.register.bean.annotation.EnableBeanMark;

/**
 * @author LHL
 * @version 1.0
 * @Description
 * @date 2022/3/1 21:50
 * @since 1.0
 */
@SpringBootApplication
@EnableBeanMark(packages = "pers.lhl.study.dynamic.register.bean.client")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
