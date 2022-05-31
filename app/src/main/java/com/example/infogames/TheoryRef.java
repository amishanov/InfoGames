package com.example.infogames;

public class TheoryRef {
    String data;
    int type; // 1 - ссылка, 0 - картинка

    public TheoryRef(String data, int type) {
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public int getType() {
        return type;
    }
}
