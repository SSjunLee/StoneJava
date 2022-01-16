package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;
import com.ljn.stone.exception.StoneException;

import java.util.List;

public class NegativeExpr extends ASTList {
    public NegativeExpr(List<ASTree> list) {
        super(list);
    }
    public ASTree operand(){
        return child(0);
    }

    @Override
    public String toString() {
        return "-"+operand();
    }

    @Override
    public Object eval(Env env) {
        Object v = operand().eval(env);
        if(v instanceof Integer){
            return -((Integer)v);
        }
        throw new StoneException("bad type for -",this);
    }
}
