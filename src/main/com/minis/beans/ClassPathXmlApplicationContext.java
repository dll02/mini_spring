package com.minis.beans;

import com.minis.beans.event.ApplicationEvent;
import com.minis.beans.event.ApplicationEventPublisher;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.SimpleBeanFactory;
import com.minis.beans.xml.ClassPathXmlResource;
import com.minis.beans.support.Resource;
import com.minis.beans.xml.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    SimpleBeanFactory beanFactory;


    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName,true);
    }
    public ClassPathXmlApplicationContext(String fileName,boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        SimpleBeanFactory beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if(isRefresh){
            this.beanFactory.refresh();
        }
    }
    @Override
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }


    public Boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }


    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerBean(beanName, obj);
    }

    public void publishEvent(ApplicationEvent event) {
    }

    public boolean isSingleton(String name) {
        return false;
    }

    public boolean isPrototype(String name) {
        return false;
    }

    public Class<?> getType(String name) {
        return null;
    }
}