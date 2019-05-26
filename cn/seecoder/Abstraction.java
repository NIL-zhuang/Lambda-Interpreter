package cn.seecoder;

import java.util.ArrayList;

public class Abstraction extends AST{
    String param;
    AST body;

    Abstraction(String param, AST body) {
        this.param = param;
        this.body = body;
    }

    public String toString() {
        return null;
    }
}
