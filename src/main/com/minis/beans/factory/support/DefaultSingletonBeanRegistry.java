package com.minis.beans.factory.support;

import com.minis.beans.factory.config.SingletonBeanRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    //容器中存放所有bean的名称的列表
    protected List<String> beanNames = new ArrayList<>();
    //容器中存放所有bean实例的map
    protected Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    protected Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
    protected Map<String,Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + singletonObject +
                        "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }

            this.singletonObjects.put(beanName, singletonObject);
            this.beanNames.add(beanName);
            System.out.println(" bean registerded............. " + beanName);
        }
    }

    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }
    public String[] getSingletonNames() {
        return (String[]) this.beanNames.toArray();
    }
    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.beanNames.remove(beanName);
            this.singletonObjects.remove(beanName);
        }
    }

    public void registerDependentBean(String beanName, String dependentBeanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
            return;
        }
        // 构建依赖 bean 的双向隐射
        // No entry yet -> fully synchronized manipulation of the dependentBeans Set
        synchronized (this.dependentBeanMap) {
            dependentBeans = this.dependentBeanMap.get(beanName);
            if (dependentBeans == null) {
                dependentBeans = new LinkedHashSet<String>(8);
                this.dependentBeanMap.put(beanName, dependentBeans);
            }
            dependentBeans.add(dependentBeanName);
        }
        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
            if (dependenciesForBean == null) {
                dependenciesForBean = new LinkedHashSet<String>(8);
                this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
            }
            dependenciesForBean.add(beanName);
        }

    }

    public boolean hasDependentBean(String beanName) {
        return this.dependentBeanMap.containsKey(beanName);
    }
    public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        return dependentBeans.toArray(new String[0]);
    }
    public String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return  dependenciesForBean.toArray(new String[0]);

    }
}