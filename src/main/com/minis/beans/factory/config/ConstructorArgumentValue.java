package com.minis.beans.factory.config;

public class ConstructorArgumentValue {
    private Object value;
    private String type;
    private String name;

    public ConstructorArgumentValue(Object value, String type) {
        this.value = value;
        this.type = type;
    }

    public ConstructorArgumentValue(String name, Object value, String type ) {
        this.value = value;
        this.type = type;
        this.name = name;
    }
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
