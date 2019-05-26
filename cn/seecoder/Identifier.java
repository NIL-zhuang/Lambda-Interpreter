package cn.seecoder;

import java.util.ArrayList;

public class Identifier extends AST {
    String value;

    Identifier(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
