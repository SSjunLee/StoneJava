package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteral extends ASTList {
    public ArrayLiteral(List<ASTree> list) {
        super(list);
    }

    int size() {
        return numChildren();
    }

    @Override
    public Object eval(Env env) {
        ArrayList<Object> arrayList = new ArrayList<>(size());
        for (ASTree item : this) {
            Object itemVal = item.eval(env);
            arrayList.add(itemVal);
        }
        return arrayList;
    }
}
