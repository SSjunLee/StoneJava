package com.ljn.stone.env;

import com.ljn.stone.exception.StoneException;

public class ArrayEnv implements Env {
    protected Env outer;
    protected Object[] values;

    public ArrayEnv(int size,Env o) {
        values = new Object[size];
        outer = o;
    }

    @Override
    public void put(int nest, int idx, Object value) {
        if (nest == 0) {
            values[idx] = value;
        } else if (outer != null) {
            outer.put(nest - 1, idx, value);
        } else {
            throw new StoneException("no outer env");
        }
    }

    @Override
    public Object get(int nest, int idx) {
        if (nest == 0) {
            return values[idx];
        } else if (outer != null) {
            return outer.get(nest - 1, idx);
        } else {
            return null;
        }
    }

    @Override
    public Symbols symbols() {
        throw new StoneException("no symbols...");
    }

    private void error() {
        throw new StoneException("no such method");
    }

    @Override
    public void put(String name, Object value) {
        error();
    }

    @Override
    public Object get(String name) {
        error();
        return null;
    }

    @Override
    public void putNew(String name, Object value) {
        error();
    }

    @Override
    public Env where(String name) {
        error();
        return null;
    }

    @Override
    public void setOuter(Env e) {
        outer = e;
    }
}
