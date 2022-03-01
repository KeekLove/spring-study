package pers.lhl.study.dynamic.register.bean.client;

import pers.lhl.study.dynamic.register.bean.annotation.BeanMark;

/**
 * @author LHL
 * @version 1.0
 * @Description
 * @date 2022/3/1 21:56
 * @since
 */
@BeanMark
public interface Teacher {
    /**
     * 获取教师信息
     * @return
     */
    String getInfo();
}
