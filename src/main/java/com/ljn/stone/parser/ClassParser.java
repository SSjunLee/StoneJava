package com.ljn.stone.parser;

import com.ljn.stone.Token;
import com.ljn.stone.ast.impl.ClassBody;
import com.ljn.stone.ast.impl.ClassStmt;
import com.ljn.stone.ast.impl.Dot;
import com.ljn.stone.ast.impl.NewStmt;
import com.ljn.stone.rules.Rule;

import static com.ljn.stone.rules.Rule.rule;

public class ClassParser extends FunctionParser {
    protected Rule member = rule().or(def, simple);
    protected Rule classBody = rule(ClassBody.class).sep("{").option(member).repeat(rule().sep(";", Token.eol).option(member))
            .sep("}");

    protected Rule defClass = rule(ClassStmt.class).sep("class")
            .identifier(reserved).option(rule().sep("extends").identifier(reserved))
            .option(rule().sep(Token.eol))
            .ast(classBody);
    protected Rule newStmt = rule(NewStmt.class).sep("new").identifier(reserved)
            .ast(postfix);

    public ClassParser() {
        program.insertChoice(defClass);
        primary.insertChoice(newStmt);
        //添加a.b.c.d
        postfix.insertChoice(rule(Dot.class).sep(".").identifier(reserved));
    }
}
