package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.Postfix;
import com.ljn.stone.env.Env;
import com.ljn.stone.exception.StoneException;

import java.util.ArrayList;
import java.util.List;

public class ArrayRef extends Postfix {
    public ArrayRef(List<ASTree> list) {
        super(list);
    }
    public ASTree index(){return child(0);}

    @Override
    public Object eval(Env env, Object value) {
        if(value instanceof ArrayList){
            ArrayList lst = (ArrayList) value;
            Object id = index().eval(env);
            if(id instanceof Integer){
                Integer i = (Integer) id;
                return lst.get(i);
            }
        }
        throw new StoneException("bad array access",this);
    }
}
