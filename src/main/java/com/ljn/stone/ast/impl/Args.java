package com.ljn.stone.ast.impl;

import com.ljn.stone.StoneFunction;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.Postfix;
import com.ljn.stone.env.Env;
import com.ljn.stone.exception.StoneException;

import java.util.List;

/**
 * 表示实参
 */
public class Args extends Postfix {
    public Args(List<ASTree> list) {
        super(list);
    }

    @Override
    public Object eval(Env callerEnv, Object function) {
        if (!(function instanceof StoneFunction)) {
            throw new StoneException("bad function ", this);
        }
        StoneFunction func = (StoneFunction) function;
        ParamList params = func.paramList(); //形参列表,形参列表记录着参数的名字
        //比较形参和实参的个数
        if (size() != params.size()) {
            throw new StoneException("bad numbers of args", this);
        }
        Env funcEnv = func.makeEnv();
        int i = 0;
        //遍历实参，实参.eval 可以获取实参的值
        for (ASTree arg : this) {
            /**
             *  f(1+2,getStuName(),"");
             *  f函数调用时，它的3个参数是在caller作用域计算出来的
             *  计算完后，创建一个新的作用域，把三个计算好的实参放到作用域里面
             */
            Object value = arg.eval(callerEnv);
            params.eval(funcEnv, i++, value);
        }
        return func.body().eval(funcEnv);
    }

    public int size() {
        return numChildren();
    }
}
