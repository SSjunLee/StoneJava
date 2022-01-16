package com.ljn.stone.env;

import java.util.HashMap;

public class Symbols {
    protected HashMap<String, Integer> table = new HashMap<>();
    protected Symbols outer;

    public Symbols() {
        this(null);
    }

    public Symbols(Symbols outer) {
        this.outer = outer;
    }

    public Integer find(String name) {
        return table.get(name);
    }

    public Integer putNew(String name) {
        Integer i = find(name);
        if (i == null) {
            return add(name);
        } else return i;
    }

    private Integer add(String name) {
        int i = table.size();
        table.put(name, i);
        return i;
    }

}
