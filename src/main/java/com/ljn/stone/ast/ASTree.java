package com.ljn.stone.ast;

import com.ljn.stone.env.Env;
import com.ljn.stone.env.Symbols;

import java.util.Iterator;

public abstract class ASTree implements Iterable<ASTree>{
    /**
     * 静态检查，主要工作是创建symbols，在symbols记录变量在数组环境中存储坐标
     * ，并将这些坐标缓存到ast节点里面
     * 指向eval方法时，我们可以直接通过坐标就从env里取出元素
     * @param symbols
     */
    public void lookUp(Symbols symbols){}
    public abstract ASTree child(int i);
    public abstract int numChildren();
    public abstract Iterator<ASTree> children();
    public abstract String location();
    public abstract Object eval(Env env);
    @Override
    public Iterator<ASTree> iterator() {
        return children();
    }
}
