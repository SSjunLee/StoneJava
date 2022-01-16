package com.ljn.stone.env;

import java.util.HashMap;

public class NestedEnv implements Env {
    protected HashMap<String, Object> map = new HashMap<>();
    protected Env outer;

    public NestedEnv() {
        this(null);
    }

    public NestedEnv(Env o) {
        outer = o;
    }

    public void setOuter(Env o) {
        outer = o;
    }


    public Env where(String name) {
        if (map.get(name) != null) return this;
        if (outer == null) return null;
        else
            return outer.where(name);
    }

    public void putNew(String name, Object value) {
        map.put(name, value);
    }


    @Override
    public void put(String name, Object value) {
        Env env = where(name);
        if (env == null) env = this;
        env.putNew(name, value);
    }

    @Override
    public Object get(String name) {


        Object v = map.get(name);
        if (v == null && outer != null)
            return outer.get(name);
        return v;
    }
}
