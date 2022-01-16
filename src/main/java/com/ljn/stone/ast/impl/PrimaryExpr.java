package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.Postfix;
import com.ljn.stone.env.Env;

import java.util.List;

public class PrimaryExpr extends ASTList {
    public PrimaryExpr(List<ASTree> list) {
        super(list);
    }
    public static ASTree create(List<ASTree> c){
        return c.size()==1?c.get(0):new PrimaryExpr(c);
    }
    public ASTree operand(){return child(0);}
    @Override
    public Object eval(Env env) {
        Object res = operand().eval(env);
        for (int i = 1; i < numChildren(); i++) {
            Postfix postfix = (Postfix) child(i);
            res= postfix.eval(env,res);
        }
        return res;
    }
}
