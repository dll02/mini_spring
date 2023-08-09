package com.minis.test.xml;

public class AServiceImpl implements AService {
    private String property1;
    private String name;
    private int level;
    private String property2;
    private BaseService ref1;

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
        System.out.println(this.name + "," + this.level);
    }

    public void setRef1(BaseService ref1) {
        this.ref1 = ref1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty1() {
        return property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public void sayHello() {
        System.out.println("a service 1 say hello:" + getProperty1() + ":" + getProperty2());
    }
}