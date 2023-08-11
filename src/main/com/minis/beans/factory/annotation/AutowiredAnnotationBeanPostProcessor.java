package com.minis.beans.factory.annotation;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        Object result = bean;

        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        //对每一个属性进行判断，如果带有@Autowired注解则进行处理
        for (Field field : fields) {
            boolean isAutowired =
                    field.isAnnotationPresent(Autowired.class);
            if (isAutowired) {
                //根据属性名查找同名的bean
                String fieldName = field.getName();
                Object autowiredObj =
                        this.getBeanFactory().getBean(fieldName);
                //设置属性值，完成注入
                try {
                    field.setAccessible(true);
                    field.set(bean, autowiredObj);
                    System.out.println("autowire " + fieldName + " for bean " + beanName+ " : " + autowiredObj + " class : "+autowiredObj.getClass());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
