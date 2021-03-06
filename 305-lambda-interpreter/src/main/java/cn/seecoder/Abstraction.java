package cn.seecoder;

public class Abstraction extends AST {
    Identifier param;
    AST body;

    Abstraction(Identifier param, AST body) {
        this.param = param;
        this.body = body;
    }

    public String toString() {
        return "\\" + "." + this.body.toString();
    }

    public String toStr() {
        return "\\" + param.name + "." + body.toStr();
    }
}
