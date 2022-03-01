package pers.lhl.study.dynamic.register.bean.client;

import pers.lhl.study.dynamic.register.bean.annotation.BeanMark;

/**
 * @author LHL
 * @version 1.0
 * @Description
 * @date 2022/3/1 21:55
 * @since
 */
@BeanMark
public interface Student {
    /**
     * 获取学生信息
     * @return
     */
    String getInfo();
}
