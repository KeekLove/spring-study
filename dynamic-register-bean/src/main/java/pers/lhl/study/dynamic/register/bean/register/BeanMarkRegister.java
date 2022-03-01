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
        // ��ȡ{@link EnableBeanMark#packages}ָ���İ�·��
        final String[] packages = attrs == null ? null : (String[]) attrs.get("packages");
        if (packages == null) {
            return;
        }
        //ʹ��classpathע��ɨ����
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false,
            this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                // ��Ϊ���������Ȳ����жϣ�ȫ��ͨ��
                return true;
            }
        };
        //������Դ������
        scanner.setResourceLoader(this.resourceLoader);
        // ����ֻɨ��BeanMarkע��
        scanner.addIncludeFilter(new AnnotationTypeFilter(BeanMark.class));
        Set<BeanDefinition> beanDefinitions = new HashSet<BeanDefinition>();
        for (String basePackage : packages) {
            beanDefinitions.addAll(scanner.findCandidateComponents(basePackage));
        }
        //����bean�������ע��
        for (BeanDefinition beanDefinition : beanDefinitions) {
            registerBeanDefinition(beanDefinition, registry);
        }
    }
    
    /**
     * beanע��
     * @param beanDefinition
     * @param registry
     */
    private void registerBeanDefinition(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        Class type = ClassUtils.resolveClassName(
            ((AnnotatedBeanDefinition) beanDefinition).getMetadata().getClassName(), null);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(type, () -> {
            //ͨ��beanʵ���ص����ɶ�Ӧ�Ĵ�����ʵ��
            return Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, new BeanMarkInvocationHandler());
        });
        BeanDefinition thisBeanDefinition = builder.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(thisBeanDefinition,
            thisBeanDefinition.getBeanClassName());
        //ע��
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
