package com.ljn.stone.ast.impl;

import com.ljn.stone.Token;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.env.Env;

public class NumberLiteral extends ASTLeaf {
    public NumberLiteral(Token token) {
        super(token);
    }
    public int value(){return token.getNumber();}

    @Override
    public Object eval(Env env) {
        return value();
    }
}
