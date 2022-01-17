package com.ljn.stone.ast.impl;

import com.ljn.stone.OptFunction;
import com.ljn.stone.StoneFunction;
import com.ljn.stone.Util;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.Postfix;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.Symbols;

import java.util.List;

public class Lamda extends ASTList {
    protected int size = -1;
    public Lamda(List<ASTree> list) {
        super(list);
    }

    ParamList paramList(){return (ParamList) child(0);}
    BlockStmt body() {return (BlockStmt) child(1);}

    @Override
    public void lookUp(Symbols symbols) {
        size = Util.lookUp(symbols,paramList(),body());
    }

    @Override
    public Object eval(Env env) {
        //return new StoneFunction(paramList(),body(),env);
        return new OptFunction(paramList(),body(),env,size);
    }
}
