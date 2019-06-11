package cn.seecoder;

public class Identifier extends AST {
    String value;
    String name;

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
