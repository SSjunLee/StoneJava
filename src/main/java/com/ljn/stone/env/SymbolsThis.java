package com.ljn.stone.env;

import com.ljn.stone.exception.StoneException;

/**
 * 记录this的保存位置
 */
public class SymbolsThis extends Symbols{

    public SymbolsThis(Symbols outer) {
        super(outer);
        add("this");
    }

    @Override
    public Integer putNew(String name) {
        throw new StoneException("fatal");
    }

    @Override
    public Location put(String k) {
        Location loc = outer.put(k);
        if(loc.nest>=0)loc.nest++;
        return loc;
    }
}
