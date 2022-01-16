package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;

import java.util.List;

public class ClassBody extends ASTList {
    public ClassBody(List<ASTree> list) {
        super(list);
    }

    @Override
    public Object eval(Env env) {
        for(ASTree stmt:this){
            stmt.eval(env);
        }
        return null;
    }
}
