package com.ljn.stone;

import com.ljn.stone.ast.ASTree;
import com.ljn.stone.env.BaseEnv;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.NestedEnv;
import com.ljn.stone.exception.ParserException;
import com.ljn.stone.naive.Naive;
import com.ljn.stone.parser.ArrayParser;
import com.ljn.stone.parser.BasicParser;
import com.ljn.stone.parser.ClassParser;
import com.ljn.stone.parser.FunctionParser;

import java.io.*;

public class Interpreter {


    private static void cmdInter(Lexer lexer, BasicParser bp, Env env) throws ParserException{
        System.out.println("----------------------------stone-----------------------------");
        while (lexer.peek(0) != Token.eof) {
            ASTree asTree = bp.parse(lexer);
            System.out.println("=>" + asTree.eval(env));
        }
    }

    private static void fileInter(Lexer lexer,BasicParser bp,Env env) throws ParserException{
        Object res = null;
        while (lexer.peek(0) != Token.eof) {
            ASTree asTree = bp.parse(lexer);
            //System.out.println(asTree + " "+asTree.getClass());
            res = asTree.eval(env);
        }
        System.out.println(res);
    }

    public static void run(BasicParser bp, Env env, String[] args) throws FileNotFoundException, ParserException {
        Lexer lexer = null;
        boolean cmdMod = false;
        if (args.length == 1) {
            String filename = args[0];
            lexer = new Lexer(new BufferedReader(new FileReader(filename)));
        } else {
            lexer = new Lexer(new InputStreamReader(System.in));
            cmdMod = true;
        }
        if (cmdMod) {
           cmdInter(lexer,bp,env);
        } else {
            fileInter(lexer,bp,env);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, ParserException {
        run(new ArrayParser(), new Naive().register(new NestedEnv()), args);
    }
}
