package com.ljn.stone.ast.impl;

import com.ljn.stone.member.StoneObject;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.Postfix;
import com.ljn.stone.env.Env;
import com.ljn.stone.exception.AccessException;
import com.ljn.stone.exception.StoneException;

import java.util.List;

public class Dot extends Postfix {
    public Dot(List<ASTree> list) {
        super(list);
    }
    public String name() {
        return ((ASTLeaf) child(0)).token().getText();
    }

    @Override
    public Object eval(Env env, Object value) {
        String member = name();
        if(value instanceof StoneObject){
            try {
               return  ((StoneObject) value).read(member);
            } catch (AccessException e) {

            }
        }
        throw new StoneException("bad member access "+member,this);
    }

}
