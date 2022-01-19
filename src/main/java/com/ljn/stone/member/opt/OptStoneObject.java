package com.ljn.stone.member.opt;


import com.ljn.stone.exception.AccessException;

import java.util.Arrays;


public class OptStoneObject {
    protected Object[] fields; //字段的值
    protected OptClassInfo classInfo;

    public OptStoneObject(OptClassInfo ci, int size) {
        fields = new Object[size];
        classInfo = ci;
    }

    public Object read(String name) throws AccessException {
        Integer i = classInfo.fieldIndex(name);
        if (i != null) return fields[i];
        i = classInfo.methodIndex(name);
        if (i != null) return method(i);
        throw new AccessException();
    }

    public Object read(Integer index) {
        return fields[index];
    }

    public void write(Integer index, Object v) {
        fields[index] = v;
    }

    public void write(String name, Object v) throws AccessException {
        Integer i = classInfo.fieldIndex(name);
        if (i != null) fields[i] = v;
        else throw new AccessException();
    }


    public Object method(int i) {
        return classInfo.method(this, i);
    }

    @Override
    public String toString() {
       return "<obj "+hashCode()+" >";
    }

    public OptClassInfo getClassInfo() {
        return classInfo;
    }
}
