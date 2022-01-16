package com.ljn.stone.ast.impl;

import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;
import com.ljn.stone.exception.StoneException;


import java.util.List;

import static com.ljn.stone.ast.impl.BoolType.TRUE;
import static com.ljn.stone.ast.impl.BoolType.FALSE;
public class BinaryExpr extends ASTList {

    public ASTree left() {
        return child(0);
    }

    public ASTree right() {
        return child(2);
    }

    public String op() {
        return ((ASTLeaf) child(1)).token().getText();
    }

    public BinaryExpr(List<ASTree> list) {
        super(list);
    }

    @Override
    public Object eval(Env env) {
        String op = op();
        if(op.equals("=")){
            Object rvalue = right().eval(env);
            return computeAssign(env,rvalue);
        }
        else {
            Object lvalue = left().eval(env);
            Object rvalue = right().eval(env);
            return computOp(lvalue,op(),rvalue);
        }
    }

    /**
     * 数字运算，字符串运算，对象比较运算
     * @param lvalue
     * @param op
     * @param rvalue
     * @return
     */
    private Object computOp(Object lvalue,String op,Object rvalue) {
        if(lvalue instanceof Integer && rvalue instanceof Integer){
            return computeNumber((Integer) lvalue,op, (Integer) rvalue);
        }else{
            if(op.equals("+"))
                return String.valueOf(lvalue) + String.valueOf(rvalue);
            else if(op.equals("==")){
                if(lvalue == null)return rvalue == null?TRUE:FALSE;
                else return lvalue.equals(rvalue)?TRUE:FALSE;
            }
        }
        throw new StoneException("bad operator", this);
    }

    private Object computeNumber(Integer a,String op,Integer b){
        if (op.equals("+"))
            return a + b;
        else if (op.equals("-"))
            return a - b;
        else if (op.equals("*"))
            return a * b;
        else if (op.equals("/"))
            return a / b;
        else if (op.equals("%"))
            return a % b;
        else if (op.equals("=="))
            return a == b ? TRUE : FALSE;
        else if (op.equals(">"))
            return a > b ? TRUE : FALSE;
        else if (op.equals("<"))
            return a < b ? TRUE : FALSE;
        else
            throw new StoneException("bad operator", this);
    }

    private Object computeAssign(Env env,Object rvalue) {
        ASTree l = left();
        if(l instanceof Name){
            Name name = (Name) l;
            env.put(name.name(),rvalue);
            return rvalue;
        }else
            throw new StoneException("bad assign"+this);
    }
}
