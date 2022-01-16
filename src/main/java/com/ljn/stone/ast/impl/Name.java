package com.ljn.stone.ast.impl;

import com.ljn.stone.Token;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.env.Env;
import com.ljn.stone.exception.StoneException;

public class Name extends ASTLeaf {
    public Name(Token token) {
        super(token);
    }
    public String name(){return token.getText();}

    @Override
    public Object eval(Env env) {
        Object value = env.get(name());
        if(value == null){
            throw new StoneException(name() + " not defined");
        }
        return value;
    }
}
