package com.minis.beans;

import com.minis.beans.config.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.config.BeanFactoryPostProcessor;
import com.minis.beans.event.ApplicationEvent;
import com.minis.beans.event.ApplicationEventPublisher;
import com.minis.beans.factory.AutowireCapableBeanFactory;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.SimpleBeanFactory;
import com.minis.beans.xml.ClassPathXmlResource;
import com.minis.beans.support.Resource;
import com.minis.beans.xml.XmlBeanDefinitionReader;

import java.util.ArrayList;
import java.util.List;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    AutowireCapableBeanFactory beanFactory;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors =
            new ArrayList<BeanFactoryPostProcessor>();

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        AutowireCapableBeanFactory beanFactory = new AutowireCapableBeanFactory();
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh) {
            this.beanFactory.refresh();
        }
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
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