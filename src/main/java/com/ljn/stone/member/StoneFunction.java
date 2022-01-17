package com.ljn.stone.member;

import com.ljn.stone.ast.impl.BlockStmt;
import com.ljn.stone.ast.impl.ParamList;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.NestedEnv;

public class StoneFunction {
    protected ParamList paramList;
    protected BlockStmt blockStmt;
    protected Env env;

    public StoneFunction(ParamList paramList, BlockStmt blockStmt, Env env) {
        this.paramList = paramList;
        this.blockStmt = blockStmt;
        this.env = env;
    }

    public ParamList paramList() {
        return paramList;
    }

    public BlockStmt body() {
        return blockStmt;
    }

    public Env makeEnv() {
        return new NestedEnv(env);
    }

    @Override
    public String toString() {
     return "<func "+hashCode()+" >";
    }
}
