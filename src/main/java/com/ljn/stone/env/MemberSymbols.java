package com.ljn.stone.env;

/**
 * 记录字段和方法的保存位置
 */
public class MemberSymbols extends Symbols {
    public static final int FIELD = -2, METHOD = -1; //方法和字段对应的上下文位置
    protected int type;

    public MemberSymbols(Symbols outer, int type) {
        super(outer);
        this.type = type;
    }

    @Override
    public Location get(String name, int nest) {
        Integer idx = table.get(name);
        if (idx != null) return new Location(type, idx);
        if (outer == null) return null;
        return outer.get(name, nest);
    }

    @Override
    public Location put(String k) {
        Location location = get(k);
        if (location == null) {
            return new Location(type, add(k));
        }
        return location;
    }
}
