package com.ljn.stone.rules;

import com.ljn.stone.Lexer;
import com.ljn.stone.Token;
import com.ljn.stone.ast.ASTLeaf;
import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;
import com.ljn.stone.exception.ParserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class Element {
    public abstract void parse(Lexer lexer, List<ASTree> res) throws ParserException;

    public abstract boolean match(Lexer lexer) throws ParserException;

    public static class Tree extends Element {
        protected Rule parser;

        public Tree(Rule parser) {
            this.parser = parser;
        }

        @Override
        public void parse(Lexer lexer, List<ASTree> res) throws ParserException {
            res.add(parser.parse(lexer));
        }

        @Override
        public boolean match(Lexer lexer) throws ParserException {
            return parser.match(lexer);
        }
    }


    public abstract static class AToken extends Element {
        protected Factory factory;

        public AToken(Class<? extends ASTLeaf> c) {
            if (c == null) c = ASTLeaf.class;
            factory = Factory.get(c, Token.class);

        }

        @Override
        public void parse(Lexer lexer, List<ASTree> res) throws ParserException {
            Token token = lexer.read();
            if (test(token)) {
                ASTree leaf = factory.make(token);
                res.add(leaf);
            } else throw new ParserException(token);
        }

        protected abstract boolean test(Token t);

        @Override
        public boolean match(Lexer lexer) throws ParserException {
            return test(lexer.peek(0));
        }
    }

    public static class IdToken extends AToken {
        protected HashSet<String> reserved; // 保留字

        public IdToken(Class<? extends ASTLeaf> c, HashSet<String> r) {
            super(c);
            reserved = r == null ? new HashSet<>() : r;
        }

        @Override
        protected boolean test(Token t) {
            return t.isIdentifier() && !reserved.contains(t.getText());
        }
    }

    public static class StrToken extends AToken {

        public StrToken(Class<? extends ASTLeaf> c) {
            super(c);
        }

        @Override
        protected boolean test(Token t) {
            return t.isString();
        }
    }


    public static class NumToken extends AToken {

        public NumToken(Class<? extends ASTLeaf> c) {
            super(c);
        }

        @Override
        protected boolean test(Token t) {
            return t.isNumber();
        }
    }

    public static class OrTree extends Element {
        protected Rule rules[];

        public OrTree(Rule[] p) {
            rules = p;
        }

        @Override
        public void parse(Lexer lexer, List<ASTree> res) throws ParserException {
            Rule rule = choose(lexer);
            if (rule == null) throw new ParserException(lexer.peek(0));
            else res.add(rule.parse(lexer));
        }

        @Override
        public boolean match(Lexer lexer) throws ParserException {
            return choose(lexer) != null;
        }

        protected Rule choose(Lexer lexer) throws ParserException {
            for (Rule r : rules) {
                if (r.match(lexer))
                    return r;
            }
            return null;
        }

        //头插
        public void insert(Rule r) {
            Rule newRules[] = new Rule[rules.length + 1];
            newRules[0] = r;
            System.arraycopy(rules, 0, newRules, 1, rules.length);
            rules = newRules;
        }
    }

    public static class Skip extends Element {
        protected String[] elements;

        public Skip(String[] es) {
            elements = es;
        }

        @Override
        public void parse(Lexer lexer, List<ASTree> res) throws ParserException {
            Token t = lexer.read();
            if (t.isIdentifier()) {
                for (String e : elements) {
                    if (e.equals(t.getText()))
                        return;
                }
            }
            if (elements.length > 0)
                throw new ParserException(elements[0] + " expect. ", t);
            else throw new ParserException(t);
        }

        @Override
        public boolean match(Lexer lexer) throws ParserException {
            Token token = lexer.peek(0);
            if (token.isIdentifier()) {
                for (String e : elements) {
                    if (e.equals(token.getText()))
                        return true;
                }
            }
            return false;
        }
    }

    public static class Precedence {
        public int value; //优先级
        public boolean leftAso; //是否左结合

        Precedence(int v, boolean l) {
            value = v;
            leftAso = l;
        }
    }

    public static class Operator extends HashMap<String, Precedence> {
        public static final boolean LEFT = true;
        public static final boolean RIGHT = false;

        public void add(String name, int value, boolean leftAso) {
            put(name, new Precedence(value, leftAso));
        }
    }

    public static class Expr extends Element {
        protected final Factory factory;
        protected final Operator ops;
        protected final Rule factor;

        public Expr(Class<? extends ASTree> clazz, Rule factor, Operator operators) {
            this.factory = Factory.getForAstList(clazz);
            this.factor = factor;
            this.ops = operators;
        }

        /**
         * 返回一颗二元表达式树
         *
         * @param lexer 一个队列，每次返回一个单词
         * @param res
         * @throws ParserException
         */
        @Override
        public void parse(Lexer lexer, List<ASTree> res) throws ParserException {
            /**
             *  factor.parse(lexer)： 从队列里取若干个单词，将其解析为一个因子
             */
            ASTree right = factor.parse(lexer);
            Precedence prec;
            /**
             *  如果说有多个运算符，需要解析这些运算符，并且把上次解析的语法树当作新的语法树的左子
             */
            while ((prec = nextOP(lexer)) != null) {
                right = doShift(right, prec.value, lexer);
            }
            res.add(right);
        }

        /**
         * 按照优先级解析运算符，返回一颗语法树
         *
         * @param left  上次解析好的语法树，这里作为新的语法树的leftson
         * @param prec  当前运算符的优先级
         * @param lexer 单词队列
         * @return
         * @throws ParserException
         */
        private ASTree doShift(ASTree left, int prec, Lexer lexer) throws ParserException {
            ArrayList<ASTree> list = new ArrayList<>();
            list.add(left);  //添加左操作数
            list.add(new ASTLeaf(lexer.read())); //添加操作符
            ASTree right = factor.parse(lexer); //消费一个单词，将其解析为因子，它就是右操作数
            Precedence next;
            //如果还有操作符，且可以解析之后的表达式，把它当作这颗语法树的右子节点
            while ((next = nextOP(lexer)) != null && rightIsExpr(next, prec)) {
                //计算右边，右边的表达式解析结果就是一颗语法树，这颗语法树作为当前语法树的右子
                right = doShift(right, next.value, lexer);
            }
            //right有两种情况，一种是单个节点，第二种是一颗语法树
            list.add(right);
            return factory.make(list);
        }

        /**
         * 判断是要先算上一个操作符还是算下一个操作符
         *
         * @param nextPrec:下一个操作符
         * @param prec：           上一个操作符
         * @return
         */
        private boolean rightIsExpr(Precedence nextPrec, int prec) {
            if (nextPrec.leftAso) {
                //a+b-c+d 应该先算左边
                return prec < nextPrec.value;
            } else {
                //a=b=c=d=e=1 应该先算右边
                return prec <= nextPrec.value;
            }
        }

        /**
         * 预读入一个单词，判断它是不是操作符
         *
         * @param lexer
         * @return
         * @throws ParserException
         */

        private Precedence nextOP(Lexer lexer) throws ParserException {
            Token t = lexer.peek(0);
            if (t.isIdentifier()) {
                return ops.get(t.getText());
            } else return null;
        }

        @Override
        public boolean match(Lexer lexer) throws ParserException {
            return factor.match(lexer);
        }
    }

    public static class Repeat extends Element {

        protected Rule rule;
        protected boolean onlyOnce;

        public Repeat(Rule r, boolean once) {
            rule = r;
            onlyOnce = once;
        }

        @Override
        public void parse(Lexer lexer, List<ASTree> res) throws ParserException {
            while (rule.match(lexer)) {
                ASTree tree = rule.parse(lexer);
                if (tree.getClass() != ASTList.class || tree.numChildren() > 0) {
                    res.add(tree);
                }
                if (onlyOnce) break;
            }
        }

        @Override
        public boolean match(Lexer lexer) throws ParserException {
            return rule.match(lexer);
        }
    }

}
