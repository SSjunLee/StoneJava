package com.ljn.stone.env;

public interface Env {
    void put(int nest,int idx,Object value);
    Object get(int nest,int idx);
    Symbols symbols();
    void put(String name,Object value);
    Object get(String name);
    void putNew(String name, Object value);
    Env where(String name);
    void setOuter(Env e);
}
