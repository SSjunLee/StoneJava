package com.ljn.stone.naive;

import com.ljn.stone.ast.ASTree;
import com.ljn.stone.exception.StoneException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NaiveFunction {
    private Method method;
    private String name;
    private int numArgs;

    public NaiveFunction(Method method, String name) {
        this.method = method;
        this.name = name;
        numArgs = method.getParameterCount();
    }

    public int numParams(){return numArgs;}

    public Object invoke(ASTree tree, Object[] args){
        try {
            return method.invoke(null,args);
        } catch (Exception e) {
            throw new StoneException("bad naive invoke :"+name,tree);
        }
    }

    @Override
    public String toString() {
       return "<naive " + hashCode() + " >";
    }
}
