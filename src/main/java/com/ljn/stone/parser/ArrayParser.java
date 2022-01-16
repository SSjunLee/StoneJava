package com.ljn.stone.parser;

import com.ljn.stone.ast.impl.ArrayLiteral;
import com.ljn.stone.ast.impl.ArrayRef;
import com.ljn.stone.rules.Rule;

import static com.ljn.stone.rules.Rule.rule;

public class ArrayParser extends ClassParser{

    Rule elements = rule(ArrayLiteral.class).ast(expr).repeat(rule().sep(",").ast(expr));
    public ArrayParser(){
        reserved.add("]");
        primary.insertChoice(rule().sep("[").maybe(elements).sep("]"));
        postfix.insertChoice(rule(ArrayRef.class).sep("[").ast(expr).sep("]"));
    }
}
