package com.ljn.stone.parser;

import com.ljn.stone.Token;
import com.ljn.stone.ast.Postfix;
import com.ljn.stone.ast.impl.Args;
import com.ljn.stone.ast.impl.DefStmt;
import com.ljn.stone.ast.impl.Lamda;
import com.ljn.stone.ast.impl.ParamList;
import com.ljn.stone.rules.Rule;
import static com.ljn.stone.rules.Rule.rule;
public class FunctionParser extends BasicParser{
    protected Rule param = rule().identifier(reserved);
    protected Rule params = rule(ParamList.class).ast(param)
                    .repeat(rule().sep(",").ast(param));
    protected Rule paramList = rule().sep("(").maybe(params).sep(")");
    protected Rule def  = rule(DefStmt.class).sep("def").identifier(reserved)
            .ast(paramList).option(rule().sep(Token.eol)).ast(block);
    protected Rule args = rule(Args.class).ast(expr).repeat(rule().sep(",").ast(expr));
    protected Rule postfix = rule().sep("(").maybe(args).sep(")");
    public FunctionParser(){
        reserved.add(")");
        // a(1)(2)...(3) 仍然是一个primary
        primary.repeat(postfix);
        // func 1,2,3
        simple.option(args);
        primary.insertChoice(rule(Lamda.class).ast(paramList).sep("=>").ast(block));
        program.insertChoice(def);
    }
}
