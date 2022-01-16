package com.ljn.stone.env;

public interface Env {
    void put(String name,Object value);
    Object get(String name);
    void putNew(String name, Object value);
    Env where(String name);
    void setOuter(Env e);
}
