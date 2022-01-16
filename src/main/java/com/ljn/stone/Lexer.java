package com.ljn.stone;

import com.ljn.stone.exception.ParserException;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {


    private final Pattern pattern = getRexPat();
    private ArrayList<Token> queue = new ArrayList<>();
    private boolean hasMore;
    private LineNumberReader reader;


    public Lexer(Reader reader) {
        this.reader = new LineNumberReader(reader);
        hasMore = true;
    }

    public Token read() throws ParserException {
        if (fillQueue(0)) return queue.remove(0);
        else return Token.eof;
    }

    public Token peek(int i) throws ParserException {
        if (fillQueue(i)) return queue.get(i);
        else return Token.eof;
    }

    private boolean fillQueue(int i) throws ParserException {
        while (i >= queue.size()) {
            if (hasMore) readLine();
            else return false;
        }
        return true;
    }


    private static String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < s.length()-1; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char c2 = s.charAt(i + 1);
                if (c2 == '"' || c2 == '\\') {
                    c = s.charAt(++i);
                } else if (c2 == 'n') {
                    ++i;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    protected void readLine() throws ParserException {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ParserException(e);
        }
        if (line == null) {
            hasMore = false;
            return;
        }

        Matcher matcher = pattern.matcher(line);
        int lineNo = reader.getLineNumber();
        matcher.useTransparentBounds(true).useAnchoringBounds(true);
        int pos = 0, endPos = line.length();
        while (pos < endPos) {
            matcher.region(pos, endPos);
            if (matcher.lookingAt()) {
                addToken(lineNo, matcher);
                pos = matcher.end();
            } else {
                throw new ParserException("bad token at line" + lineNo);
            }
        }
        queue.add(new IdToken(lineNo, Token.eol));
    }

    private void addToken(int lineNo, Matcher matcher) {
        String m = matcher.group(1);
        if (m != null) {
            //不是空格
            if (matcher.group(2) == null) {
                //不是注释
                Token token;
                if (matcher.group(3) != null) {
                    token = new NumToken(lineNo, Integer.parseInt(matcher.group(3)));
                } else if (matcher.group(4) != null) {
                    token = new StrToken(lineNo, toStringLiteral(matcher.group(4)));
                } else {
                    token = new IdToken(lineNo, m);
                }
                queue.add(token);
            }
        }
    }


    private Pattern getRexPat() {
        final String numPat = "([0-9]+)";
        final String spacePat = "\\s*";
        final String commentPat = "(//.*)";
        /**
         * strInsidePat与 \"、\\、\n和除“ 之外任意一个字符匹配
         * strInsidePat ="(\\"|\\\\|\\n|[^"])*"
         */
        final String strInsidePat = "(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")";
        /**
         * identifyPat =  [A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\|\||\p{Punct}
         */
        final String identifyPat = "([A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})";
        String reg = spacePat + "(" + commentPat + "|" + numPat + "|" + strInsidePat + "|" + identifyPat + ")";
        return Pattern.compile(reg);
    }

    private static class IdToken extends Token {
        private String text;

        protected IdToken(int line) {
            super(line);
        }

        public IdToken(int line, String text) {
            super(line);
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public boolean isIdentifier() {
            return true;
        }
    }


    private static class StrToken extends Token {
        private String text;

        protected StrToken(int line) {
            super(line);
        }

        public StrToken(int line, String text) {
            super(line);
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public boolean isString() {
            return true;
        }
    }

    private static class NumToken extends Token {
        private int value;

        protected NumToken(int line) {
            super(line);
        }

        public NumToken(int line, int value) {
            super(line);
            this.value = value;
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public int getNumber() {
            return value;
        }

        @Override
        public String getText() {
            return Integer.toString(value);
        }
    }

}
