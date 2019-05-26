package cn.seecoder;

public class Lexer {

    String source;
    int index;
    Token token;


    /**
     * 构造第一个Token，相当于head
     */
    public Lexer(String s) {
        this.source = s;
        this.index = 0;
        this.token = new Token();
        this.token.tokenType = TokenType.UNDEFINED;
        this.nextToken();
    }

    /**
     * return the next char of the input or '#' if the we reach the end of the source
     */
    private char nextChar() {
        if (this.index >= this.source.length()) {
            return '#';
        }
        return this.source.charAt(this.index++);
    }

    /**
     * set this.token on the basis of the remaining of the input
     * return a token, and set a fundamental for the helper functions
     */
    public Token nextToken() {
        char temp = ' ';
        //跳过所有空格
        while (temp == ' ') {
            temp = this.nextChar();
        }
        //判断token类型
        switch (temp) {
            case '\\':
                this.token = new Token(TokenType.LAMBDA, String.valueOf(temp));
                break;
            case '.':
                this.token = new Token(TokenType.DOT, String.valueOf(temp));
                break;
            case '(':
                this.token = new Token(TokenType.LPAREN, String.valueOf(temp));
                break;
            case ')':
                this.token = new Token(TokenType.RPAREN, String.valueOf(temp));
                break;
            case '#':
                this.token = new Token(TokenType.EOF, String.valueOf(temp));
                break;
            default:
                if ('a' <= temp && 'z' >= temp || 'A' <= temp && 'Z' >= temp) {
                    StringBuilder stringBuilder = new StringBuilder();
                    //处理value是一个字符串的情况
                    while ('a' <= temp && 'z' >= temp || 'A' <= temp && 'Z' >= temp) {
                        stringBuilder.append(temp);
                        temp = this.nextChar();
                    }
                    this.index--;
                    //put back the last char if it isn't part of the identifier
                    this.token = new Token(TokenType.LCID, stringBuilder.toString());
                    break;
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

    public boolean match(TokenType type) {
        if (this.next(type)) {
            this.nextToken();
            return true;
        }
        return false;
    }

    public Token token(TokenType type) {
        if (next(type)) {
            return this.token;
        } else {
            throw new Error("wrong");
        }
    }

    public static void main(String[] args) {
        String source = "(\\x.\\y.x)(\\x.x)(\\y.y)";
        Lexer lexer = new Lexer(source);
        while (!lexer.token.value.equals("#")) {
            lexer.nextToken();
        }
    }
}
