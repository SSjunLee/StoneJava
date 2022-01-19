package com.ljn.stone.ast.impl;

import com.ljn.stone.env.MemberSymbols;
import com.ljn.stone.env.Symbols;
import com.ljn.stone.env.SymbolsThis;
import com.ljn.stone.member.StoneClassInfo;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;
import com.ljn.stone.member.opt.OptClassInfo;

import java.util.ArrayList;
import java.util.List;

public class ClassStmt extends ASTList {
    @Override
    public void lookUp(Symbols symbols) {

    }

    public ClassStmt(List<ASTree> list) {
        super(list);
    }

    public String name() {
        return ((ASTLeaf) child(0)).token().getText();
    }

    public String superClass() {
        if (numChildren() < 3) return null;
        return ((ASTLeaf) child(1)).token().getText();
    }

    public ClassBody body() {
        return (ClassBody) child(numChildren() - 1);
    }

    @Override
    public String toString() {
        String father = superClass();
        return "(class " + father + " " + name() + " )";
    }


    @Override
    public Object eval(Env env) {
        /*StoneClassInfo classInfo = new StoneClassInfo(this,env);
        env.put(classInfo.name(),classInfo);
        return name();*/
        Symbols methods = new MemberSymbols(env.symbols(), MemberSymbols.METHOD);
        Symbols fields = new MemberSymbols(methods, MemberSymbols.FIELD);
        OptClassInfo optClassInfo = new OptClassInfo(this, env, methods, fields);
        env.put(optClassInfo.name(), optClassInfo);
        ArrayList<DefStmt> defStmts = new ArrayList<>();
        //将父类的字段和方法拷贝到当前类里
        if (optClassInfo.superClass() != null)
            ((OptClassInfo) optClassInfo.superClass()).copyOf(methods, fields, defStmts);

        SymbolsThis symbolsThis = new SymbolsThis(fields);
        //symbolsThis（this pos） -> symbolsFiled（字段名pos） -> symbolsMethods(方法名pos) -> symbols(保存变量名与相应的保存位置)
        body().lookUp(symbolsThis, methods, fields, defStmts);
        optClassInfo.setMethods(defStmts.toArray(new DefStmt[0]));

        return name();
    }
}
