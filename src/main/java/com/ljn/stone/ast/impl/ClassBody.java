package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.Symbols;
import com.ljn.stone.env.SymbolsThis;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存字段和方法
 */
public class ClassBody extends ASTList {
    public ClassBody(List<ASTree> list) {
        super(list);
    }

    @Override
    public Object eval(Env env) {
        for (ASTree stmt : this) {
            stmt.eval(env);
        }
        return null;
    }

    public void lookUp(SymbolsThis symbolsThis, Symbols methods, Symbols fields, ArrayList<DefStmt> defStmts) {
        for (ASTree t : children) {
            if (t instanceof DefStmt) {
                DefStmt defStmt = (DefStmt) t;
                int oldSize = methods.size();
                int i = methods.putNew(defStmt.name());
                if(i>=oldSize){
                    defStmts.add(defStmt);
                }else{
                    defStmts.set(i,defStmt);
                }
                //方法里可能引用到了字段
                defStmt.lookUpAsMethod(fields);
            } else {
                t.lookUp(symbolsThis);
            }
        }
    }
}
