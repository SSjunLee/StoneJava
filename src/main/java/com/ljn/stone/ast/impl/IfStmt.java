package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;

import java.util.List;

import static com.ljn.stone.ast.impl.BoolType.FALSE;

public class IfStmt extends ASTList {

    public IfStmt(List<ASTree> list) {
        super(list);
    }

    @Override
    public Object eval(Env env) {
        Object cond = condition().eval(env);
        if (cond instanceof Integer && (Integer) cond != FALSE) {
            return thenBlock().eval(env);
        } else {
            if (elseBlock() != null)
                return elseBlock().eval(env);
            return null;
        }
    }

    public ASTree condition() {
        return child(0);
    }

    public ASTree thenBlock() {
        return child(1);
    }

    public ASTree elseBlock() {
        return numChildren() > 2 ? child(2) : null;
    }

    @Override
    public String toString() {
        return "(if con: " + condition() + " ," + thenBlock() + "else " + elseBlock() + " )";
    }
}
