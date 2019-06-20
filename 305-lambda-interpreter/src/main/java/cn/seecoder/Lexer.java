package cn.seecoder;

public class Lexer {

    private String source;
    private int index;
    Token token;


    /**
     * 构造第一个Token，相当于head
     */
    public Lexer(String s) {
        testBracket(s);         //检测输入的字符串括号匹配
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
    private Token nextToken() {
        char temp = ' ';
        while (temp == ' ') {                //跳过所有空格
            temp = this.nextChar();
        }
        switch (temp) {                     //判断token类型
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
                    StringBuilder stringBuilder = new StringBuilder();                      //处理value是一个字符串的情况
                    while ('a' <= temp && 'z' >= temp || 'A' <= temp && 'Z' >= temp) {
                        stringBuilder.append(temp);
                        temp = this.nextChar();
                        if (temp == '#') {
                            return new Token("#");
                        }
                    }
                    this.index--;                                               //put back the last char if it isn't part of the identifier
                    this.token = new Token(TokenType.LCID, stringBuilder.toString());
                    break;
                } else {
                    throw new Error("Unexpected token");
                }
        }
        System.out.println(this.token.tokenType);
        return token;
    }

    /**
     * 判断是否符合期望的类型
     *
     * @param type 期望的token类型
     * @return 是否符合这样的类型
     */
    boolean next(TokenType type) {
        return this.token.tokenType == type;
    }

    /**
     * 判断是否符合类型，同时token flow进到下一个
     *
     * @param type 期望的类型
     * @return 是否符合这样的类型
     */
    boolean getNextToken(TokenType type) {
        if (this.next(type)) {
            this.nextToken();
            return true;
        }
        return false;
    }

    private void testBracket(String s) {
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                index++;
            } else if (s.charAt(i) == ')') {
                index--;
            }
            if (index < 0) {
                throw new Error("Bracket doesn't match");
            }
        }
        if (index != 0) {
            throw new Error("Bracket doesn't match");
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
