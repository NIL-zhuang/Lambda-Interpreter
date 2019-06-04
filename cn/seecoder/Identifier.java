package cn.seecoder;

import java.util.ArrayList;

public class Identifier extends AST {
    String value;
    String name;

    Identifier(String value) {
        this.value = value;
    }

    Identifier(String name, String value) {
        this.value = value;
        this.name = name;
    }


    public String toString() {
        return this.value ;
    }
}
