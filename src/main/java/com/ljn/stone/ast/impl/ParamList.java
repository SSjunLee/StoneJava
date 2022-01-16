package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;

import java.util.List;

public class ParamList extends ASTList {
    public ParamList(List<ASTree> list) {
        super(list);
    }

    public String name(int i){
        return ((ASTLeaf)child(i)).token().getText();
    }
    public int size(){return numChildren();}

    /**
     *
     * @param env 调用函数的环境
     * @param i 第几个参数
     * @param v 参数的值
     */
    public void eval(Env env,int i,Object v){
        env.putNew(name(i),v);
    }
}
