package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;

import java.util.Iterator;
import java.util.List;

public class BlockStmt extends ASTList {
    public BlockStmt(List<ASTree> list) {
        super(list);
    }

    @Override
    public Object eval(Env env) {
        Iterator<ASTree> it = children();
        Object res = null;
        while (it.hasNext()){
            ASTree cur = it.next();
            if(!(cur instanceof NullStmt)){
                res = cur.eval(env);
            }
        }
        return res;
    }
}
