package com.ljn.stone.ast.impl;

import com.ljn.stone.StoneClassInfo;
import com.ljn.stone.Token;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;

import java.util.List;

public class ClassStmt extends ASTList {
    public ClassStmt(List<ASTree> list) {
        super(list);
    }
    public String name(){return ((ASTLeaf) child(0)).token().getText();}
    public String superClass(){
        if(numChildren()<3)return null;
        return  ((ASTLeaf) child(1)).token().getText();
    }
    public ClassBody body(){
        return (ClassBody) child(numChildren()-1);
    }

    @Override
    public String toString() {
        String father = superClass();
        return "(class "+father+" "+hashCode()+" )";
    }

    @Override
    public Object eval(Env env) {
        StoneClassInfo classInfo = new StoneClassInfo(this,env);
        env.put(classInfo.name(),classInfo);
        return name();
    }
}
