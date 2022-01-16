package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;

import java.util.List;

import static com.ljn.stone.ast.impl.BoolType.FALSE;

public class WhileStmt extends ASTList {

    public WhileStmt(List<ASTree> list) {
        super(list);
    }

    public ASTree condition() {
        return child(0);
    }

    public ASTree body() {
        return child(1);
    }

    @Override
    public String toString() {
        return "(while con: " + condition() + " ," + body() + " )";
    }

    @Override
    public Object eval(Env env) {
       for(;;){
           Object con = condition().eval(env);
           if(con instanceof Integer && (Integer) con == FALSE)
               return null;
           else
               body().eval(env);
       }
    }
}
