package cn.seecoder;

public class Parser {
    Lexer lexer;
    Term term;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    Term Parse() {
        this.lexer.match(TokenType.EOF);
        return this.term;
    }

    public AST parse() {
        return null;
    }

}
