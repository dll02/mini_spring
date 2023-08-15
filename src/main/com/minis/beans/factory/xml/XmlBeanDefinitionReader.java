package com.minis.beans.factory.xml;

import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.config.*;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    AbstractBeanFactory bf;

    public XmlBeanDefinitionReader(AbstractBeanFactory bf) {
        this.bf = bf;
    }

    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            String initMethod = element.attributeValue("init-method");

            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
            beanDefinition.setInitMethodName(initMethod);

            List<Element> propertyElements = element.elements("property");
            List<String> refs = new ArrayList<>();
            PropertyValues PVS = new PropertyValues();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";

                boolean isRef = false;
                if (pValue != null && !pValue.equals("")) {
                    isRef = false;
                    pV = pValue;
                } else if (pRef != null && !pRef.equals("")) {
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new PropertyValue(pName, pV, pType, isRef));
            }
            beanDefinition.setPropertyValues(PVS);
            //处理构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String aType = e.attributeValue("type");
                String aName = e.attributeValue("name");
                String aValue = e.attributeValue("value");
                AVS.addArgumentValue(new ConstructorArgumentValue(aName, aValue, aType));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            this.bf.registerBeanDefinition(beanDefinition.getId(),beanDefinition);
        }
    }
}