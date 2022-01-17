package com.ljn.stone.ast.impl;

import com.ljn.stone.Token;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.Symbols;
import com.ljn.stone.exception.StoneException;

public class Name extends ASTLeaf {
    protected static final int unknown = -1;
    protected int nest, index;

    public Name(Token token) {
        super(token);
        index = unknown;
    }

    public String name() {
        return token.getText();
    }

    /**
     * 读变量时会调用这个方法
     * @param symbols
     */
    @Override
    public void lookUp(Symbols symbols) {
        Symbols.Location location = symbols.get(name());
        if(location == null){
            throw new StoneException(name() + " undefined");
        }
        nest = location.nest;
        index = location.index;
    }

    public void evalForAssign(Env env,Object v){
        if(index == unknown){
            env.put(name(),v);
        }else{
            env.put(nest,index,v);
        }
    }

    public void lookUpForAssign(Symbols symbols){
        Symbols.Location loc = symbols.put(name());
        index = loc.index;
        nest = loc.nest;
    }

    @Override
    public Object eval(Env env) {
        if (index == unknown) {
            return env.get(name());
        }
        return env.get(nest, index);
        /*
        Object value = env.get(name());
        if(value == null){
            throw new StoneException(name() + " not defined");
        }
        return value;*/
    }
}
