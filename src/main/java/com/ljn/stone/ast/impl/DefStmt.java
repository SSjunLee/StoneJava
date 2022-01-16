package com.ljn.stone.ast.impl;

import com.ljn.stone.StoneFunction;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;

import java.util.List;

public class DefStmt extends ASTList {
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
    public Object eval(Env env) {
        env.putNew(name(),new StoneFunction(paramList(),body(),env));
        return name();
    }
}
