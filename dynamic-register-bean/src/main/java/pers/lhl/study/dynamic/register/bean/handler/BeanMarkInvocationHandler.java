package pers.lhl.study.dynamic.register.bean.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author LHL
 * @version 1.0
 * @Description
 * @date 2022/3/1 22:16
 * @since
 */
public class BeanMarkInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 返回代理方法名称
        return method.toGenericString();
    }
}
