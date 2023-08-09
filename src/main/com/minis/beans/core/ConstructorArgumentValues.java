package com.minis.beans.core;

import java.util.*;

public class ConstructorArgumentValues {
    private final List<ConstructorArgumentValue> genericConstructorArgumentValues = new LinkedList<>();

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(ConstructorArgumentValue newValue) {
        this.genericConstructorArgumentValues.add(newValue);
    }

    //    public boolean hasIndexedArgumentValue(int index) {
//        return this.indexedArgumentValues.containsKey(index);
//    }
    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return this.genericConstructorArgumentValues.get(index);
    }

    public void addGenericArgumentValue(Object value, String type) {
        this.genericConstructorArgumentValues.add(new ConstructorArgumentValue(value, type));
    }

    private void genericArgumentValues(ConstructorArgumentValue newValue) {
        if (newValue.getName() != null) {
            for (Iterator<ConstructorArgumentValue> it =
                 this.genericConstructorArgumentValues.iterator(); it.hasNext(); ) {
                ConstructorArgumentValue currentValue = it.next();
                if (newValue.getName().equals(currentValue.getName())) {
                    it.remove();
                }
            }
        }
        this.genericConstructorArgumentValues.add(newValue);
    }

    public ConstructorArgumentValue getGenericArgumentValue(String requiredName) {
        for (ConstructorArgumentValue valueHolder : this.genericConstructorArgumentValues) {
            if (valueHolder.getName() != null && (requiredName == null ||
                    !valueHolder.getName().equals(requiredName))) {
                continue;
            }
            return valueHolder;
        }
        return null;
    }

    public int getArgumentCount() {
        return this.genericConstructorArgumentValues.size();
    }

    public boolean isEmpty() {
        return this.genericConstructorArgumentValues.isEmpty();
    }
}