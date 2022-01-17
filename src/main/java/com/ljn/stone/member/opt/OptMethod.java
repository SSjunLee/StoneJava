package com.ljn.stone.member.opt;

import com.ljn.stone.ast.impl.BlockStmt;
import com.ljn.stone.ast.impl.ParamList;
import com.ljn.stone.env.ArrayEnv;
import com.ljn.stone.env.Env;

public class OptMethod extends OptFunction{
    protected OptStoneObject self;
    public OptMethod(ParamList paramList, BlockStmt blockStmt, Env env, int size,OptStoneObject self) {
        super(paramList, blockStmt, env, size);
        this.self = self;
    }

    @Override
    public Env makeEnv() {
        ArrayEnv arrayEnv = new ArrayEnv(size,env);
        arrayEnv.put(0,0,self);
        return arrayEnv;
    }
}
