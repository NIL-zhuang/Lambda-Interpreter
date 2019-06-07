package cn.seecoder;

import java.util.ArrayList;

public class Identifier extends AST {
    String value;
    String name;

    Identifier(String value) {
        this.value = value;
    }

    Identifier(String name, String value) {
        this.name = name;
        this.value = value;

    }


    public String toString() {
        return this.value;
    }

    public String toStr() {
        return this.name;
    }
}
