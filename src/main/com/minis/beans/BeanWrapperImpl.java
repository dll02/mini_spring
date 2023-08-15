package com.minis.beans;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanWrapperImpl extends AbstractPropertyAccessor {
    Object wrappedObject;
    Class<?> clz;

    public BeanWrapperImpl(Object object) {
        super();
        this.wrappedObject = object;
        this.clz = object.getClass();
    }

    /**
     * 处理 请求参数对象的 bean field 字段对象的set
     *
     * @param pv
     */
    @Override
    public void setPropertyValue(PropertyValue pv) {
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        PropertyEditor pe = this.getCustomEditor(propertyHandler.getPropertyClz());
        if (pe == null) {
            pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
        }
        if (pe != null) {
            pe.setAsText((String) pv.getValue());
            propertyHandler.setValue(pe.getValue());
        } else {
            propertyHandler.setValue(pv.getValue());
        }
    }

    class BeanPropertyHandler {
        Method writeMethod = null;
        Method readMethod = null;
        Class<?> propertyClz = null;

        public Class<?> getPropertyClz() {
            return propertyClz;
        }

        public BeanPropertyHandler(String propertyName) {
            try {
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();
                this.writeMethod = clz.getDeclaredMethod("set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
                this.readMethod = clz.getDeclaredMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
            } catch (NoSuchMethodException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }

        public Object getValue() {
            Object result = null;
            writeMethod.setAccessible(true);

            try {
                result = readMethod.invoke(wrappedObject);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return result;

        }

        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

}
