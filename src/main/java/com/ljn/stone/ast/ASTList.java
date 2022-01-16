package com.ljn.stone.ast;

import com.ljn.stone.env.Env;
import com.ljn.stone.exception.StoneException;

import java.util.Iterator;
import java.util.List;

public class ASTList extends ASTree {
    protected List<ASTree> children;

    public ASTList(List<ASTree> list) {
        children = list;
    }

    @Override
    public ASTree child(int i) {
        return children.get(i);
    }

    @Override
    public int numChildren() {
        return children.size();
    }

    @Override
    public Iterator<ASTree> children() {
        return children.iterator();
    }

    @Override
    public String location() {
        for(ASTree c:children){
            if(c.location()!=null)return c.location();
        }
        return null;
    }

    @Override
    public Object eval(Env env) {
        throw new StoneException("can't eval "+toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        String sep = "";
        for(ASTree t:children){
            sb.append(sep);
            sep = " ";
            sb.append(t.toString());
        }
        return sb.append(")").toString();
    }
}
