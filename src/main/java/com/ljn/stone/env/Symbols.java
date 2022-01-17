package com.ljn.stone.env;

import java.util.HashMap;

public class Symbols {
    public static class Location{
        public int nest,index;
        public Location(int nest,int index){
            this.nest = nest;
            this.index = index;
        }
    }

    protected HashMap<String, Integer> table = new HashMap<>();
    protected Symbols inner;

    public Symbols() {
        this(null);
    }

    public Symbols(Symbols outer) {
        this.inner = outer;
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

    public int size() {
        return table.size();
    }

    public Location get(String name){
        return get(name,0);
    }

    public Location get(String name,int nest){
        Integer index = table.get(name);
        if(index == null){
            if(inner == null)
                return null;
            return inner.get(name,nest+1);
        }
        return new Location(nest,index);
    }

    public Location put(String k){
        Location location = get(k);
        if(location == null){
            return new Location(0,add(k));
        }
        return location;
    }
}
