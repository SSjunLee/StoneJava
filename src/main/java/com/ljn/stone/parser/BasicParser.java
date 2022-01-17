package com.ljn.stone.parser;

import com.ljn.stone.Lexer;
import com.ljn.stone.Token;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.ast.impl.*;
import com.ljn.stone.exception.ParserException;
import com.ljn.stone.rules.Element;
import com.ljn.stone.rules.Rule;

import java.util.HashSet;

import static com.ljn.stone.rules.Rule.rule;

public class BasicParser {
    protected HashSet<String> reserved = new HashSet<>();
    protected Rule expr0 = rule();
    protected Rule primary = rule(PrimaryExpr.class)
            .or(rule().sep("(").ast(expr0).sep(")"),
                    rule().number(NumberLiteral.class), rule().identifier(Name.class, reserved),
                    rule().string(StringLiteral.class));
    protected Rule factor = rule().or(rule(NegativeExpr.class).sep("-").ast(primary), primary);
    protected Element.Operator operators = new Element.Operator();
    protected Rule expr = expr0.expression(BinaryExpr.class, factor, operators);
    protected Rule statement0 = rule();
    protected Rule block = rule(BlockStmt.class).sep("{").option(statement0)
            .repeat(rule().sep(";", Token.eol).option(statement0)).sep("}");
    protected Rule simple = rule(PrimaryExpr.class).ast(expr);
    protected Rule statement = statement0.or(
            rule(IfStmt.class).sep("if").ast(expr).option(rule().sep(Token.eol)).ast(block).option(
                    rule().sep("else").ast(block)
            ),
            rule(WhileStmt.class).sep("while").ast(expr).option(rule().sep(Token.eol)).ast(block),
            simple);
    protected Rule program = rule().or(statement, rule(NullStmt.class)).sep(";", Token.eol);

    public BasicParser() {
        reserved.add(";");
        reserved.add(Token.eol);
        reserved.add("}");
        reserved.add(")");


        operators.add("=", 1, Element.Operator.RIGHT);
        operators.add("==", 2, Element.Operator.LEFT);
        operators.add(">", 2, Element.Operator.LEFT);
        operators.add("<", 2, Element.Operator.LEFT);
        operators.add("+", 3, Element.Operator.LEFT);
        operators.add("-", 3, Element.Operator.LEFT);
        operators.add("*", 4, Element.Operator.LEFT);
        operators.add("/", 4, Element.Operator.LEFT);
        operators.add("%", 4, Element.Operator.LEFT);
        operators.add(">=", 2, Element.Operator.LEFT);
        operators.add("<=", 2, Element.Operator.LEFT);


    }

    public ASTree parse(Lexer lexer) throws ParserException {
        return program.parse(lexer);
    }
}
