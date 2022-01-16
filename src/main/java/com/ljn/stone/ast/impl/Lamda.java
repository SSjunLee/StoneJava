package com.ljn.stone.ast.impl;

import com.ljn.stone.StoneFunction;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.Postfix;
import com.ljn.stone.env.Env;

import java.util.List;

public class Lamda extends ASTList {
    public Lamda(List<ASTree> list) {
        super(list);
    }

    ParamList paramList(){return (ParamList) child(0);}
    BlockStmt body() {return (BlockStmt) child(1);}

    @Override
    public Object eval(Env env) {
        return new StoneFunction(paramList(),body(),env);
    }
}
