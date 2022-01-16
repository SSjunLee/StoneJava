package com.ljn.stone.exception;

import com.ljn.stone.Token;

import java.io.IOException;

public class ParserException extends Exception{
    public ParserException(String msg, Token t){
        super("syntax error around "+location(t)+". "+msg);
    }

    public ParserException(IOException e) {
        super(e);
    }

    public ParserException(String s) {
        super(s);
    }

    public ParserException(Token peek) {
        super(peek.getText());
    }

    private static String location(Token t){
        if(t == Token.eof){
            return "the last line";
        }else {
            return "\""+t.getText()+"\" at line "+t.getLineNumber();
        }
    }

}
