package com.ljn.stone.member;

import com.ljn.stone.ast.impl.ClassBody;
import com.ljn.stone.ast.impl.ClassStmt;
import com.ljn.stone.env.Env;
import com.ljn.stone.exception.StoneException;

public class StoneClassInfo {
    private final ClassStmt classDef;
    private final StoneClassInfo parent;
    private Env env;

    public StoneClassInfo(ClassStmt classDef, Env env) {
        this.classDef = classDef;
        if (classDef.superClass() != null) {
            Object parent = env.get(classDef.superClass());
            if (parent != null) {
                if (parent instanceof StoneClassInfo)
                    this.parent = (StoneClassInfo) parent;
                else
                    throw new StoneException("unknown super class " + classDef.superClass(), classDef);
            } else {
                throw new StoneException("unknown super class " + classDef.superClass(), classDef);
            }
        } else {
            this.parent = null;
        }
        this.env = env;
    }

    public String name() {
        return classDef.name();
    }

    public ClassBody body() {
        return classDef.body();
    }

    public StoneClassInfo superClass() {
        return parent;
    }

    public Env env() {
        return env;
    }

    @Override
    public String toString() {
        return "<class " + name() + " " + parent + " " + body() + " >";
    }
}
