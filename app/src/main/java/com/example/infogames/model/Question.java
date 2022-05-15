package com.example.infogames.model;

import java.util.Arrays;

public class Question {
    private String question;
    // Если type = 1, то вопрос с ручным вводом варианта ответа, если 2 - с выбором
    private int type;
    private String answer;
    private String[] answers;
    private int rightAnswer;

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
        type = 1;
    }

    public Question() {}

    @Override
    public String toString() {
        return "{" +
                "\"question\": " + "\"" +question + "\"" +
                ", \"type\": " + "\""+ type +"\"" +
                ", \"answer\": " + "\"" + answer + "\"" +
                ", \"answers\": " + Arrays.toString(answers) +
                ", \"rightAnswer\": " + "\"" + rightAnswer + "\"" +
                '}';
    }

    public Question(String question, String[] answers, int rightAnswer) {
        this.question = question;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
        type = 2;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getType() {
        return type;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
