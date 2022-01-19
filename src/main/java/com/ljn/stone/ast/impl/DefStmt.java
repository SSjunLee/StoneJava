package com.ljn.stone.ast.impl;

import com.ljn.stone.env.SymbolsThis;
import com.ljn.stone.member.opt.OptFunction;
import com.ljn.stone.Util;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.Symbols;

import java.util.List;

public class DefStmt extends ASTList {
    protected int index, size;

    public DefStmt(List<ASTree> list) {
        super(list);
    }

    //返回局部变量的个数
    public Integer locals() {
        return size;
    }

    public String name() {
        return ((ASTLeaf) child(0)).token().getText();
    }

    public ParamList paramList() {
        return (ParamList) child(1);
    }

    public BlockStmt body() {
        return (BlockStmt) child(2);
    }

    @Override
    public String toString() {
        return "( def " + name() + " , " + paramList() + " , " + body() + " )";
    }

    @Override
    public void lookUp(Symbols symbols) {
        index = symbols.putNew(name());
        size = Util.lookUp(symbols, paramList(), body());
    }

    @Override
    public Object eval(Env env) {
        //env.putNew(name(),new StoneFunction(paramList(),body(),env));
        env.put(0, index, new OptFunction(paramList(), body(), env, size));
        return name();
    }

    /**
     * 作为类的方法，需要记录this，和方法内变量的位置
     * @param fieldNames
     */
    public void lookUpAsMethod(Symbols fieldNames) {
        Symbols newSymbol = new Symbols(fieldNames);
        //这里和Utils.lookUp 差不多，唯一的不同是放入this
        newSymbol.putNew(SymbolsThis.NAME);
        paramList().lookUp(newSymbol);
        body().lookUp(newSymbol);
        size = newSymbol.size();//更新局部变量的个数
    }
}
