package org.wmc.integrated.scope;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.stream.Stream;

@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ScopedBeanDemo {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(new Class[]{ScopedBeanDemo.class, ThreadLocalBeanFactoryPostProcessor.class});
        context.refresh();
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        /**
         * beanName : org.springframework.context.annotation.internalConfigurationAnnotationProcessor -----> beanType：org.springframework.context.annotation.ConfigurationClassPostProcessor
         * beanName : org.springframework.context.annotation.internalAutowiredAnnotationProcessor -----> beanType：org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
         * beanName : org.springframework.context.annotation.internalCommonAnnotationProcessor -----> beanType：org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
         * beanName : org.springframework.context.event.internalEventListenerProcessor -----> beanType：org.springframework.context.event.EventListenerMethodProcessor
         * beanName : org.springframework.context.event.internalEventListenerFactory -----> beanType：org.springframework.context.event.DefaultEventListenerFactory
         * beanName : scopedTarget.scopedBeanDemo -----> beanType：org.wmc.scope.ScopedBeanDemo
         * beanName : scopedBeanDemo -----> beanType：org.wmc.scope.ScopedBeanDemo$$EnhancerBySpringCGLIB$$be3fb0c
         */
        Stream.of(beanDefinitionNames)
                .forEach(
                        beanName -> {
                            Class<?> beanType = context.getType(beanName);
                            System.out.printf("beanName : %s -----> beanType：%s \n",beanName,beanType.getName());
                        });


        // 根据 ScopedBeanDemo 类型来查找
        ScopedBeanDemo byType = context.getBean(ScopedBeanDemo.class);
        // 根据 ScopedBeanDemo 在IoC容器中的BeanName来进行查找 -> 其底层也是通过 Java Beans 中的
        // Introspector#decapitalize方法来生成BeanName
        ScopedBeanDemo byName =
                (ScopedBeanDemo) context.getBean(Introspector.decapitalize("ScopedBeanDemo")); // scopedBeanDemo
        // 在 ScopedBeanDemo 在IoC容器中的BeanName 前面拼接上 ScopedProxyUtils#TARGET_NAME_PREFIX 字段的值
        Field field = ScopedProxyUtils.class.getDeclaredField("TARGET_NAME_PREFIX");
        Object value = field.get(null);
        ScopedBeanDemo byScopedName =
                (ScopedBeanDemo)
                        context.getBean(value + Introspector.decapitalize("ScopedBeanDemo"));
        /**
         * 根据ScopedBeanDemo类型查找到的：class org.wmc.scope.ScopedBeanDemo$$EnhancerBySpringCGLIB$$be3fb0c
         * 根据ScopedBeanDemo名称查找到的：class org.wmc.scope.ScopedBeanDemo$$EnhancerBySpringCGLIB$$be3fb0c
         * 根据scopedTarget.ScopedBeanDemo名称查找到的：class org.wmc.scope.ScopedBeanDemo
         */
        System.out.println("根据ScopedBeanDemo类型查找到的：" + byType);
        System.out.println("根据ScopedBeanDemo名称查找到的：" + byName);
        System.out.println("根据scopedTarget.ScopedBeanDemo名称查找到的：" + byScopedName.getClass());
        System.out.println("根据scopedTarget.ScopedBeanDemo名称查找到的："  + byScopedName);
        System.out.println("thread name:" + Thread.currentThread().getName());
        // 关闭Spring 应用上下文
        // context.close();
    }
}