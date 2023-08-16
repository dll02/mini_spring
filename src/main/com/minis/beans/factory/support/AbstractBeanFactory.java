package com.minis.beans.factory.support;

import com.minis.beans.BeansException;
import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.FactoryBean;
import com.minis.beans.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory
        extends FactoryBeanRegistrySupport
        implements ConfigurableBeanFactory, BeanDefinitionRegistry {
    public Map<String, BeanDefinition> beanDefinitionMap = new
            ConcurrentHashMap<>(256);
    public List<String> beanDefinitionNames = new ArrayList<>();
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public AbstractBeanFactory() {
    }

    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        //先尝试直接从容器中获取bean实例
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            //如果没有实例，则尝试从毛胚实例中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                //如果连毛胚都没有，则创建bean实例并注册
                System.out.println("get bean null -------------- " + beanName);
                BeanDefinition bd = beanDefinitionMap.get(beanName);
                if (bd != null) {
                    singleton = createBean(bd);
                    this.registerBean(beanName, singleton);
                    if (singleton instanceof BeanFactoryAware) {
                        ((BeanFactoryAware) singleton).setBeanFactory(this);
                    }
                    // 进行beanpostprocessor处理
                    // step 1: postProcessBeforeInitialization
                    singleton = applyBeanPostProcessorsBeforeInitialization(singleton, beanName);
                    System.out.println(" class proxy after bean post processor " + singleton.getClass());
                    // step 2: run init-method with bean
                    if (bd.getInitMethodName() != null &&
                            !bd.getInitMethodName().isEmpty()) {
                        invokeInitMethod(bd, singleton);
                    }
                    // step 3: postProcessAfterInitialization
                    applyBeanPostProcessorsAfterInitialization(singleton, beanName);
                    this.removeSingleton(beanName);
                    this.registerBean(beanName, singleton);
                }
            }
        }

        // 处理factorybean
        if (singleton instanceof FactoryBean) {
            System.out.println("factory bean -------------- " + beanName + "----------------" + singleton);
            return this.getObjectForBeanInstance(singleton, beanName);
        } else {
            System.out.println("normal bean -------------- " + beanName + "----------------" + singleton);

        }
        return singleton;
    }

    private void invokeInitMethod(BeanDefinition beanDefinition, Object obj) {
        Class<?> clz = obj.getClass();
        Method method = null;
        try {
            method = clz.getMethod(beanDefinition.getInitMethodName());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            method.invoke(obj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean containsBean(String name) {
        return containsSingleton(name);
    }

    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition
            beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
//        if (!beanDefinition.isLazyInit()) {
//            try {
//                getBean(name);
//            } catch (BeansException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }


    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        //创建毛胚bean实例
        Object obj = doCreateBean(beanDefinition);
        //存放到毛胚实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //完善bean，主要是处理属性
        populateBean(beanDefinition, clz, obj);
        return obj;
    }

    //doCreateBean创建毛胚实例，仅仅调用构造方法，没有进行属性处理
    private Object doCreateBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;
        try {
            clz = Class.forName(bd.getClassName());
            // handle constructor
            ConstructorArgumentValues argumentValues = bd.getConstructorArgumentValues();
            if (argumentValues!=null && !argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>
                        [argumentValues.getArgumentCount()];
                Object[] paramValues = new
                        Object[argumentValues.getArgumentCount()];
                for (int i = 0; i <
                        argumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue argumentValue =
                            argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) ||
                            "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if
                    ("Integer".equals(argumentValue.getType()) ||
                                    "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String)
                                argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String)
                                argumentValue.getValue());
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);
            } else {
                obj = clz.newInstance();
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        System.out.println(bd.getId() + " bean created. " +
                bd.getClassName() + " : " + obj.toString());
        return obj;
    }

    private void populateBean(BeanDefinition beanDefinition, Class<?> clz,
                              Object obj) {
        handleProperties(beanDefinition, clz, obj);
    }

    private void handleProperties(BeanDefinition beanDefinition, Class<?> clz,
                                  Object obj) {
        // handle properties
        System.out.println("handle properties for bean : " +
                beanDefinition.getId());
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        //如果有属性
        if (propertyValues != null && !propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue =
                        propertyValues.getPropertyValueList().get(i);
                String pType = propertyValue.getType();
                String pName = propertyValue.getName();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if (!isRef) { //如果不是ref，只是普通属性
                    //对每一个属性，分数据类型分别处理
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                        paramValues[0] = pValue;
                    }
                    else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                        paramValues[0] = Integer.valueOf((String) pValue);
                    }
                    else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                        paramValues[0] = Integer.valueOf((String) pValue).intValue();
                    }
                    else {
                        paramTypes[0] = String.class;
                        paramValues[0] = pValue;
                    }
                } else {//is ref, create the dependent beans
                    try {
                        paramTypes[0] = Class.forName(pType);
                        paramValues[0] = getBean((String)pValue);
                    } catch (ClassNotFoundException |BeansException e) {
                        e.printStackTrace();
                    }
                }
                //按照setXxxx规范查找setter方法，调用setter方法设置属性
                String methodName = "set" + pName.substring(0, 1).toUpperCase()
                        + pName.substring(1);
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                try {
                    method.invoke(obj, paramValues);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // Now we have the bean instance, which may be a normal bean or a FactoryBean.
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        Object object = null;
        FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
        object = getObjectFromFactoryBean(factory, beanName);
        return object;
    }

    abstract public Object applyBeanPostProcessorsBeforeInitialization(
            Object existingBean, String beanName) throws BeansException;

    abstract public Object applyBeanPostProcessorsAfterInitialization(
            Object existingBean, String beanName) throws BeansException;
}