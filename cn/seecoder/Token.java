package cn.seecoder;


public class Token {
    TokenType tokenType;
    String value;

    Token() {
    }

    Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }
}
