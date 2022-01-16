package com.ljn.stone;

/**
 * 表示单词
 * 总共有三种单词：1.整型字面量 2.字符型字面量 3. 标识符
 */
public abstract class Token {
    public static final Token eof = new Token(-1){};
    public static final String eol = "\\n";

    private int lineNumber;
    protected Token(int line){
        lineNumber = line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public boolean isIdentifier(){return false;}
    public boolean isNumber(){return false;}
    public boolean isString(){return false;}

    public int getNumber(){throw new RuntimeException("not number token");}
    public String getText(){return "";}

}
