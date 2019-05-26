package cn.seecoder;

public class Application extends AST {
    String param;
    AST body;
    AST lhs;
    AST rhs;

    Application(AST lhs, AST rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String toString() {
        return null;
    }
}
