package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.Postfix;
import com.ljn.stone.env.Env;

import java.util.List;

public class ArrayRef extends Postfix {
    public ArrayRef(List<ASTree> list) {
        super(list);
    }

    @Override
    public Object eval(Env env, Object value) {
        return null;
    }
}
