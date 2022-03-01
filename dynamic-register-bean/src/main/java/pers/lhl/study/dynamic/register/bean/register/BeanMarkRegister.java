package pers.lhl.study.dynamic.register.bean.register;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import pers.lhl.study.dynamic.register.bean.annotation.BeanMark;
import pers.lhl.study.dynamic.register.bean.annotation.EnableBeanMark;
import pers.lhl.study.dynamic.register.bean.handler.BeanMarkInvocationHandler;

import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @author LHL
 * @version 1.0
 * @Description
 * @date 2022/3/1 22:01
 * @since
 */
public class BeanMarkRegister implements ResourceLoaderAware, EnvironmentAware, ImportBeanDefinitionRegistrar {
    
    private ResourceLoader resourceLoader;
    
    private Environment environment;
    
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnableBeanMark.class.getName());
        // 获取{@link EnableBeanMark#packages}指定的包路径
        final String[] packages = attrs == null ? null : (String[]) attrs.get("packages");
        if (packages == null) {
            return;
        }
        //使用classpath注解扫描器
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false,
            this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                // 作为案例这里先不做判断，全部通过
                return true;
            }
        };
        //设置资源加载器
        scanner.setResourceLoader(this.resourceLoader);
        // 过滤只扫描BeanMark注解
        scanner.addIncludeFilter(new AnnotationTypeFilter(BeanMark.class));
        Set<BeanDefinition> beanDefinitions = new HashSet<BeanDefinition>();
        for (String basePackage : packages) {
            beanDefinitions.addAll(scanner.findCandidateComponents(basePackage));
        }
        //根据bean定义进行注册
        for (BeanDefinition beanDefinition : beanDefinitions) {
            registerBeanDefinition(beanDefinition, registry);
        }
    }
    
    /**
     * bean注册
     * @param beanDefinition
     * @param registry
     */
    private void registerBeanDefinition(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        Class type = ClassUtils.resolveClassName(
            ((AnnotatedBeanDefinition) beanDefinition).getMetadata().getClassName(), null);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(type, () -> {
            //通过bean实例回调生成对应的代理类实例
            return Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, new BeanMarkInvocationHandler());
        });
        BeanDefinition thisBeanDefinition = builder.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(thisBeanDefinition,
            thisBeanDefinition.getBeanClassName());
        //注册
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }
    
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
