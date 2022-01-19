package com.ljn.stone.ast.impl;

import com.ljn.stone.member.StoneObject;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.Postfix;
import com.ljn.stone.env.Env;
import com.ljn.stone.exception.AccessException;
import com.ljn.stone.exception.StoneException;
import com.ljn.stone.member.opt.OptClassInfo;
import com.ljn.stone.member.opt.OptStoneObject;

import java.util.List;

public class Dot extends Postfix {
    protected static final int FIELD = 1, METHOD = 2;
    protected int type;
    protected int index;
    protected OptClassInfo classInfo = null;

    public Dot(List<ASTree> list) {
        super(list);
    }

    public String name() {
        return ((ASTLeaf) child(0)).token().getText();
    }

    @Override
    public Object eval(Env env, Object value) {
        String member = name();
        if (value instanceof OptStoneObject) {
            OptStoneObject so = (OptStoneObject) value;
            //缓存不一致，则更新缓存
            if (so.getClassInfo() != classInfo) {
                updateCache(so);
            }
            if (type == FIELD) {
                return so.read(index);
            } else if (type == METHOD) {
                return so.method(index);
            }
        }
        throw new StoneException("bad member access " + member, this);
    }

    private void updateCache(OptStoneObject so) {
        String member = name();
        classInfo = so.getClassInfo();
        Integer idx = classInfo.fieldIndex(member);
        if (idx != null) {
            index = idx;
            type = FIELD;
            return;
        }
        idx = classInfo.methodIndex(member);
        if (idx != null) {
            index = idx;
            type = METHOD;
            return;
        }
        throw new StoneException("bad member access " + member, this);
    }

}
