package com.ljn.stone.rules;

import com.ljn.stone.Lexer;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.exception.ParserException;

import java.util.ArrayList;
import java.util.HashSet;

public class Rule {

    private ArrayList<Element> elements;
    private Factory factory;


    public Rule(Class<? extends ASTree> c) {
        reset(c);
    }

    public Rule(Rule p) {
        this.factory = p.factory;
        this.elements = p.elements;
    }

    public ASTree parse(Lexer lexer) throws ParserException {
        ArrayList<ASTree> res = new ArrayList<>();
        for (Element e : elements) {
            e.parse(lexer, res);
        }
        return factory.make(res);
    }

    public boolean match(Lexer lexer) throws ParserException {
        if (elements.size() == 0) return true;
        else {
            return elements.get(0).match(lexer);
        }
    }

    public Rule reset() {
        elements = new ArrayList<>();
        return this;
    }

    public Rule reset(Class<? extends ASTree> clazz) {
        elements = new ArrayList<>();
        factory = Factory.getForAstList(clazz);
        return this;
    }

    public static Rule rule() {
        return rule(null);
    }

    public static Rule rule(Class<? extends ASTree> c) {
        return new Rule(c);
    }

    public Rule number() {
        return number(null);
    }

    public Rule number(Class<? extends ASTLeaf> c) {
        elements.add(new Element.NumToken(c));
        return this;
    }

    public Rule identifier(HashSet<String> reserved) {
        return identifier(null, reserved);
    }

    public Rule identifier(Class<? extends ASTLeaf> c, HashSet<String> reserved) {
        elements.add(new Element.IdToken(c, reserved));
        return this;
    }

    public Rule string() {
        return string(null);
    }

    public Rule string(Class<? extends ASTLeaf> c) {
        elements.add(new Element.StrToken(c));
        return this;
    }

    public Rule or(Rule ... rules){
        elements.add(new Element.OrTree(rules));
        return this;
    }

    public Rule ast(Rule r){
        elements.add(new Element.Tree(r));
        return this;
    }

    public Rule sep(String ...pat){
        elements.add(new Element.Skip(pat));
        return this;
    }

    public Rule expression(Class<? extends ASTree> clazz, Rule factor, Element.Operator operators){
        elements.add(new Element.Expr(clazz,factor,operators));
        return this;
    }

    //0次或1次
    public Rule option(Rule r){
        elements.add(new Element.Repeat(r,true));
        return this;
    }
    //0次或多次
    public Rule repeat(Rule r){
        elements.add(new Element.Repeat(r,false));
        return this;
    }

    public Rule maybe(Rule r){
        Rule r2 = new Rule(r);
        r2.reset();
        elements.add(new Element.OrTree(new Rule[]{r,r2}));
        return this;
    }

    public Rule insertChoice(Rule r){
        Element e = elements.get(0);
        if(e instanceof Element.OrTree){
            ((Element.OrTree)e).insert(r);
        }else{
            Rule rule = new Rule(this);
            reset();
            or(r,rule);
        }
        return this;
    }

}
