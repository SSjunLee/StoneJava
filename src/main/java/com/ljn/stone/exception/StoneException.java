package com.ljn.stone.exception;

import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.impl.BinaryExpr;

public class StoneException extends RuntimeException{

    public StoneException(String s) {
        super(s);
    }

    public StoneException(String m, ASTree t) {
        super(m+" "+t.location());
    }
}
