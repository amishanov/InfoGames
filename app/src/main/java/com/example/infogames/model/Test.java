package com.example.infogames.model;

import java.util.ArrayList;

public class Test {
    private int testId;
    private ArrayList<Question> questionList;

    public Test(int testId, ArrayList<Question> questionList) {
        this.testId = testId;
        this.questionList = questionList;
    }
}
