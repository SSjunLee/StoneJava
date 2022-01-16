package com.ljn.stone.ast.impl;

import com.ljn.stone.Token;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.env.Env;

public class StringLiteral extends ASTLeaf {
    public StringLiteral(Token token) {
        super(token);
    }
    public String value(){return token.getText();}

    @Override
    public Object eval(Env env) {
        return value();
    }
}
