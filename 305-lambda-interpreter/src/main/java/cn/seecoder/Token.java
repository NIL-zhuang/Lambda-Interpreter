package cn.seecoder;

class Token {
    TokenType tokenType;
    String value;

    Token() {
        this.tokenType = TokenType.UNDEFINED;
    }

    Token(String value) {
        this.value = value;
    }

    Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }
}
