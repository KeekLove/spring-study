package pers.lhl.study.dynamic.register.bean.service;

import org.springframework.beans.factory.annotation.Autowired;
import pers.lhl.study.dynamic.register.bean.client.Student;
import pers.lhl.study.dynamic.register.bean.client.Teacher;

import javax.annotation.PostConstruct;

/**
 * @author LHL
 * @version 1.0
 * @Description
 * @date 2022/3/1 21:55
 * @since
 */
@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private Student student;
    
    @Autowired
    private Teacher teacher;
    
    @PostConstruct
    public void post(){
        System.out.println(student.getInfo());
        System.out.println(teacher.getInfo());
    }
}