package com.ljn.stone.env;

import java.util.Arrays;

public class ResizeArrayEnv extends ArrayEnv{

    protected Symbols names;
    public ResizeArrayEnv(int size) {
        super(size,null);
        names = new Symbols();
    }
    public Symbols names(){return names;}

    @Override
    public Object get(String name) {
        Integer i = names.find(name);
        if(i == null){
            if(outer == null)
                return null;
            else return outer.get(name);
        }else{
            return values[i];
        }
    }

    @Override
    public void put(String name, Object value) {
        Env env = where(name);
        if(env == null){
            //如果外面作用域没有，则在当前作用域定义变量
            env = this;
        }
        env.putNew(name,value);
    }

    @Override
    public void putNew(String name, Object value) {
        assign(names.putNew(name),value);
    }

    @Override
    public void put(int nest, int idx, Object value) {
        if(nest == 0){
            assign(idx,value);
        }
        super.put(nest, idx, value);
    }

    protected void assign(int index, Object value){
        if(index>=values.length){
            int newLen = 2* values.length;
            if(index>=newLen)newLen = index+1;
            values = Arrays.copyOf(values,newLen);
        }
        values[index] = value;
    }
}
