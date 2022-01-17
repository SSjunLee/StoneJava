package com.ljn.stone;

import com.ljn.stone.ast.impl.BlockStmt;
import com.ljn.stone.ast.impl.ParamList;
import com.ljn.stone.env.ArrayEnv;
import com.ljn.stone.env.Env;

public class OptFunction extends StoneFunction{
    protected int size;
    public OptFunction(ParamList paramList, BlockStmt blockStmt, Env env,int size) {
        super(paramList, blockStmt, env);
        this.size = size;
    }
    @Override
    public Env makeEnv() {
        return new ArrayEnv(size,env);
    }
}
