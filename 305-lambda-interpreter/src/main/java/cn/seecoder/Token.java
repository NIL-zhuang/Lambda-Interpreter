package cn.seecoder;

public class Token {
    TokenType tokenType;
    String value;

    Token() {
        this.tokenType = TokenType.UNDEFINED;
    }

    Token(TokenType type) {
        this.tokenType = type;
    }

    Token(String value) {
        this.value = value;
    }

    Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public String tokenValue() {
        return this.value;
    }
}
