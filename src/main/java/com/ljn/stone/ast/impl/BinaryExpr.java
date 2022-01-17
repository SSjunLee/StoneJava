package com.ljn.stone.ast.impl;

import com.ljn.stone.member.StoneObject;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.Symbols;
import com.ljn.stone.exception.AccessException;
import com.ljn.stone.exception.StoneException;


import java.util.ArrayList;
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
        if (op.equals("=")) {
            Object rvalue = right().eval(env);
            return computeAssign(env, rvalue);
        } else {
            Object lvalue = left().eval(env);
            Object rvalue = right().eval(env);
            return computOp(lvalue, op(), rvalue);
        }
    }

    /**
     * 数字运算，字符串运算，对象比较运算
     *
     * @param lvalue
     * @param op
     * @param rvalue
     * @return
     */
    private Object computOp(Object lvalue, String op, Object rvalue) {
        if (lvalue instanceof Integer && rvalue instanceof Integer) {
            return computeNumber((Integer) lvalue, op, (Integer) rvalue);
        } else {
            if (op.equals("+"))
                return String.valueOf(lvalue) + String.valueOf(rvalue);
            else if (op.equals("==")) {
                if (lvalue == null) return rvalue == null ? TRUE : FALSE;
                else return lvalue.equals(rvalue) ? TRUE : FALSE;
            }
        }
        throw new StoneException("bad operator", this);
    }

    private Object computeNumber(Integer a, String op, Integer b) {
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
        else if (op.equals(">="))
            return a >= b ? TRUE : FALSE;
        else if (op.equals("<="))
            return a <= b ? TRUE : FALSE;
        else
            throw new StoneException("bad operator", this);
    }

    private Object setFiled(StoneObject so, String filed, Object value) {
        try {
            so.write(filed, value);
            return value;
        } catch (AccessException e) {
            throw new StoneException("bad member access " + filed, this);
        }
    }


    private Object memberAssign(PrimaryExpr primary, Env env, Object rvalue) {
        /**
         * 处理对象成员赋值
         */
        // a.b.c.d.e = x  这里相当于取出 （a.b.c.d）
        // 如果是a.b = x  相当于取出a
        Object t = primary.evalSubExpr(1, env);
        if (t instanceof StoneObject) {
            StoneObject so = (StoneObject) t;
            Dot dot = (Dot) primary.getPostfix(0); //这里dot就是e，即最右边
            return setFiled(so, dot.name(), rvalue);
        }
        throw new StoneException("bad assign member");
    }


    private Object arrAssign(PrimaryExpr primary, Env env, Object rvalue) {
        /**
         * 处理数组赋值
         */
        Object t = primary.evalSubExpr(1, env);
        if (t instanceof ArrayList) {
            ArrayList lst = (ArrayList) t;
            ArrayRef arrayRef = (ArrayRef) primary.getPostfix(0);
            Object idxObj = arrayRef.index().eval(env);
            if (idxObj instanceof Integer) {
                try {
                    lst.set((Integer) idxObj, rvalue);
                } catch (Exception e) {
                    throw new StoneException("index out of range", this);
                }
                return rvalue;
            }
        }
        throw new StoneException("bad access array", this);
    }


    @Override
    public void lookUp(Symbols symbols) {
        ASTree left = left();
        if (op().equals("=")){
            if(left instanceof Name){
                Name l = (Name) left;
                l.lookUpForAssign(symbols);
                right().lookUp(symbols);
                return;
            }
        }
        left.lookUp(symbols);
        right().lookUp(symbols);
    }

    private Object computeAssign(Env env, Object rvalue) {
        ASTree l = left();
        if (l instanceof PrimaryExpr) {
            PrimaryExpr primary = (PrimaryExpr) l;
            if (primary.hasPostfix(0) && primary.getPostfix(0) instanceof Dot) {
                return memberAssign(primary, env, rvalue);
            } else if (primary.hasPostfix(0) && primary.getPostfix(0) instanceof ArrayRef) {
                return arrAssign(primary, env, rvalue);
            }
        }
        if (l instanceof Name) {
            Name name = (Name) l;
            name.evalForAssign(env,rvalue);
            //env.put(name.name(), rvalue);
            return rvalue;
        } else
            throw new StoneException("bad assign" + this);
    }
}
