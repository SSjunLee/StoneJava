package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.Symbols;

import java.util.List;

/**
 * 形参
 */
public class ParamList extends ASTList {
    protected int offset[] = null;
    public ParamList(List<ASTree> list) {
        super(list);
    }

    public String name(int i){
        return ((ASTLeaf)child(i)).token().getText();
    }
    public int size(){return numChildren();}

    @Override
    public void lookUp(Symbols symbols) {
        int size= size();
        offset = new int[size];
        for (int i = 0; i < size; i++) {
            offset[i] = symbols.putNew(name(i));
        }
    }

    /**
     *
     * @param env 调用函数的环境
     * @param i 第几个参数
     * @param v 参数的值
     */
    public void eval(Env env,int i,Object v){
        //env.putNew(name(i),v);
        env.put(0,offset[i],v);
    }
}
