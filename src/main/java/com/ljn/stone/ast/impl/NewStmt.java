package com.ljn.stone.ast.impl;

import com.ljn.stone.member.StoneClassInfo;
import com.ljn.stone.member.StoneObject;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.NestedEnv;
import com.ljn.stone.exception.StoneException;

import java.util.List;

public class NewStmt extends ASTList {
    public NewStmt(List<ASTree> list) {
        super(list);
    }

    public String name() {
        return ((ASTLeaf) child(0)).token().getText();
    }

    private StoneClassInfo classInfo(Env env) {
        String className = name();
        Object c = env.get(className);
        if (c == null) throw new StoneException(className + " not defined ", this);
        if (c instanceof StoneClassInfo) {
            return (StoneClassInfo) c;
        }
        throw new StoneException(className + " is not a class ", this);
    }


    @Override
    public Object eval(Env env) {
        Env newEnv = new NestedEnv(env);
        StoneObject stoneObject = new StoneObject(newEnv);
        newEnv.putNew("this", stoneObject);
        StoneClassInfo c = classInfo(env);
        initObject(c, newEnv);
        return stoneObject;
    }

    private void initObject(StoneClassInfo c, Env newEnv) {
        if (c.superClass() != null) {
            initObject(c.superClass(), newEnv);
        }
        c.body().eval(newEnv);
    }
}
