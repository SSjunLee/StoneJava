package com.ljn.stone.ast.impl;

import com.ljn.stone.OptFunction;
import com.ljn.stone.StoneFunction;
import com.ljn.stone.Util;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.Symbols;

import java.util.List;

public class DefStmt extends ASTList {
    protected int index,size;

    public DefStmt(List<ASTree> list) {
        super(list);
    }

    public String name(){
        return ((ASTLeaf)child(0)).token().getText();
    }
    public ParamList paramList(){return (ParamList) child(1);}
    public BlockStmt body(){return (BlockStmt) child(2);}

    @Override
    public String toString() {
        return "( def "+name()+" , "+paramList()+" , "+body()+" )";
    }

    @Override
    public void lookUp(Symbols symbols) {
        index = symbols.putNew(name());
        size = Util.lookUp(symbols,paramList(),body());
    }

    @Override
    public Object eval(Env env) {
        //env.putNew(name(),new StoneFunction(paramList(),body(),env));
        env.put(0,index,new OptFunction(paramList(),body(),env,size));
        return name();
    }
}
