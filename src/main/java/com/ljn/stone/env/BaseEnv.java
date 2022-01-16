package com.ljn.stone.env;

import java.util.HashMap;

public class BaseEnv implements Env{
    protected HashMap<String,Object> map = new HashMap<>();
    @Override
    public void put(String name, Object value) {
        map.put(name,value);
    }

    @Override
    public Object get(String name) {
        return map.get(name);
    }

    @Override
    public void putNew(String name, Object value) {

    }

    @Override
    public Env where(String name) {
        return null;
    }

    @Override
    public void setOuter(Env e) {

    }
}
