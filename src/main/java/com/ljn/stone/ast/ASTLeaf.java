package com.ljn.stone.ast;

import com.ljn.stone.Token;
import com.ljn.stone.env.Env;
import com.ljn.stone.exception.StoneException;

import java.util.ArrayList;
import java.util.Iterator;

public class ASTLeaf extends ASTree{
    private static ArrayList<ASTree> empty = new ArrayList<>();
    protected Token token;
    public ASTLeaf(Token token){this.token = token;}

    @Override
    public ASTree child(int i) {
       throw new IndexOutOfBoundsException();
    }

    @Override
    public int numChildren() {
        return 0;
    }

    @Override
    public Iterator<ASTree> children() {
        return empty.iterator();
    }

    @Override
    public String location() {
        return "at line "+token.getLineNumber();
    }

    @Override
    public Object eval(Env env) {
        throw new StoneException("can't eval "+toString());
    }

    @Override
    public String toString() {
        return token.getText();
                //+" ["+ getClass()+"] ";
    }

    public Token token(){return token;}
}
