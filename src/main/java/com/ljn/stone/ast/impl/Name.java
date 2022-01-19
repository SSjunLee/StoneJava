package com.ljn.stone.ast.impl;

import com.ljn.stone.Token;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.MemberSymbols;
import com.ljn.stone.env.Symbols;
import com.ljn.stone.env.SymbolsThis;
import com.ljn.stone.exception.StoneException;
import com.ljn.stone.member.opt.OptStoneObject;

public class Name extends ASTLeaf {
    protected static final int unknown = -100;
    protected int nest, index;//nest 代表从当前frame往外面走多少步

    public Name(Token token) {
        super(token);
        index = unknown;
    }

    public String name() {
        return token.getText();
    }


    /**
     * 对右值的静态检查，先看看有没有定义，并取出它的位置坐标
     *
     * @param symbols
     */
    @Override
    public void lookUp(Symbols symbols) {
        Symbols.Location location = symbols.get(name());
        if (location == null) {
            throw new StoneException(name() + " undefined");
        }
        nest = location.nest;
        index = location.index;
    }

    public void evalForAssign(Env env, Object v) {

        if (index == unknown) {
            env.put(name(), v);
        } else if (nest == MemberSymbols.FIELD) {
            getThis(env).write(index, v);
        } else if (nest == MemberSymbols.METHOD) {
            throw new StoneException("can't update for method", this);
        } else
            env.put(nest, index, v);
    }

    /**
     * 赋值对左值的静态检查，如果存在，则返回位置，否则，新建对象，并返回位置
     *
     * @param symbols
     */
    public void lookUpForAssign(Symbols symbols) {
        Symbols.Location loc = symbols.put(name());
        index = loc.index;
        nest = loc.nest;
    }

    @Override
    public Object eval(Env env) {
        if (index == unknown) {
            return env.get(name());
        } else if (index == MemberSymbols.FIELD) {
            return getThis(env).read(index);
        } else if (index == MemberSymbols.METHOD) {
            return getThis(env).method(index);
        }
        return env.get(nest, index);
    }

    protected OptStoneObject getThis(Env env) {
        return (OptStoneObject) env.get(0, 0);
    }

}
