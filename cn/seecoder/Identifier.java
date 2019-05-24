package cn.seecoder;

import java.util.ArrayList;

public class Identifier extends Term {
    String value;

    Identifier(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
