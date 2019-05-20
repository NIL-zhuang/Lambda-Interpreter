package cn.seecoder;

public class Lexer {

    public String source;
    public int index;
    public Token token;

    public Lexer(String s) {
        this.source = s;
        this.index = 0;
        this.nextToken();
    }

    /**
     * @return the next char of the input or '#' if the we reach the end of the source
     */
    private char nextChar() {
        if (this.index >= this.source.length()) {
            return '#';
        }
        return this.source.charAt(this.index++);
    }

    /**
     * set this.token on the basis of the remaining of the input
     *
     * @return a token, and set a fundamental for the helper functions
     */
    private Token nextToken() {
        Token token = new Token();
        while (this.nextChar() == ' ') {
            token.value = String.valueOf(source.charAt(index));
        }
        /*
        define a patten and test if it's LCID
         */
        switch (source.charAt(index)) {
            case '\\':
                token.tokenType = TokenType.LAMBDA;
                break;
            case '.':
                token.tokenType = TokenType.DOT;
                break;
            case '(':
                token.tokenType = TokenType.LPAREN;
                break;
            case ')':
                token.tokenType = TokenType.RPAREN;
                break;
            case '#':
                token.tokenType = TokenType.EOF;
                break;
            default:
                if (isEnglish(source.charAt(index))) {
                    StringBuilder sBuilder = new StringBuilder();
                    while (isEnglish(this.nextChar())) {
                        sBuilder.append(this.nextChar());
                    }
                    this.index--;
                    // the last char isn't a part of the identifier, you have to put it back
                    this.token = new Token(TokenType.LCID, sBuilder.toString());
                } else {
                    throw new Error("Unexpected token");
                }
        }
        return token;
    }

    public boolean next(TokenType type) {
        return this.token.tokenType == type;
    }

    public boolean skip(TokenType type) {
        if (this.next(type)) {
            this.nextToken();
            return true;
        }
        return false;
    }

    public void match(TokenType type) {
        if (this.next(type)) {
            this.nextToken();
            return;
        }
        throw new Error("Parse Error");
    }

    public String token(TokenType type) {
        if (!next(type)) {
            return this.token.value;
        }
        token = this.token;
        this.match(type);
        return token.value;
    }

    private boolean isEnglish(char c) {
        return ('a' < c && 'z' > c || 'A' < c && 'Z' > c);
    }
}
