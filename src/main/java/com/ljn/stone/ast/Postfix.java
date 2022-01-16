package com.ljn.stone.ast;

import com.ljn.stone.env.Env;

import java.util.List;

public abstract class  Postfix extends ASTList {
    public Postfix(List<ASTree> list) {
        super(list);
    }
    public abstract Object eval(Env env, Object value);
}
