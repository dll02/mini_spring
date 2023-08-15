package com.minis.web.bind;

 import java.util.Map;

 import com.minis.beans.AbstractPropertyAccessor;
 import com.minis.beans.BeanWrapperImpl;
 import com.minis.beans.PropertyEditor;
 import com.minis.beans.PropertyValues;
import com.minis.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;

public class WebDataBinder {
    private Object target;
    private Class<?> clz;
    private String objectName;
    AbstractPropertyAccessor propertyAccessor;
    public WebDataBinder(Object target) {
        this(target, "");
    }
    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.objectName = targetName;
        this.clz = this.target.getClass();
        this.propertyAccessor = new BeanWrapperImpl(this.target);
    }
    //核心绑定方法，将request里面的参数值绑定到目标对象的属性上
    public void bind(HttpServletRequest request) {
        PropertyValues mpvs = assignParameters(request);
        addBindValues(mpvs, request);
        doBind(mpvs);
    }
    private void doBind(PropertyValues mpvs) {
        applyPropertyValues(mpvs);
    }
    //实际将参数值与对象属性进行绑定的方法
    protected void applyPropertyValues(PropertyValues mpvs) {
        getPropertyAccessor().setPropertyValues(mpvs);
    }
    //设置属性值的工具
    protected BeanWrapperImpl getPropertyAccessor() {
        return new BeanWrapperImpl(this.target);
    }
    //将Request参数解析成PropertyValues
    private PropertyValues assignParameters(HttpServletRequest request) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }
    protected void addBindValues(PropertyValues mpvs, HttpServletRequest request) {
    }

    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }
}