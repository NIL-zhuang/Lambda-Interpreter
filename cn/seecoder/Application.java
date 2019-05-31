package cn.seecoder;

public class Application extends AST {
    AST lhs;
    AST rhs;

    Application(AST lhs, AST rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String toString() {
        if (lhs == null) {
            return (rhs == null) ? null : "(" + rhs.toString() + ")";
        } else {
            return (rhs == null) ? "(" + lhs.toString() + ")" : "(" + (lhs.toString() + " " + rhs.toString()) + ")";
        }
    }
}
