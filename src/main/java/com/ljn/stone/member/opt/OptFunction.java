package com.ljn.stone.member.opt;

import com.ljn.stone.ast.impl.BlockStmt;
import com.ljn.stone.ast.impl.ParamList;
import com.ljn.stone.env.ArrayEnv;
import com.ljn.stone.env.Env;
import com.ljn.stone.member.StoneFunction;

public class OptFunction extends StoneFunction {
    protected int size;//方法局部变量的个数
    public OptFunction(ParamList paramList, BlockStmt blockStmt, Env env,int size) {
        super(paramList, blockStmt, env);
        this.size = size;
    }
    @Override
    public Env makeEnv() {
        return new ArrayEnv(size,env);
    }
}
