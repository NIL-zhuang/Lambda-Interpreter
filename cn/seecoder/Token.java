package cn.seecoder;


public class Token {
    TokenType tokenType;
    char value;

    Token(TokenType tokenType, char value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public void skip() {
    }

    public boolean match() {

        return true;
    }

    public Token token() {

        return null;
    }

}
