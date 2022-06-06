package com.example.infogames;

import com.example.infogames.model.Question;

public class QuestionGenerator {


    public static Question generateBinaryToDecimal() {
        Question question = new Question();
        question.setType(2);
        int answer =  (int) (Math.random() * 100);
        int[] answers = new int[4];
        int rAnswer = (int) (Math.random() * 4);
        answers[0] = answer;
        for (int i = 0; i < answers.length; i++) {
            if (i == rAnswer)
                answers[i] = answer;
            else if ((int) (Math.random() * 2) == 1)
                answers[i] = answer + (int) (Math.random() * 10 + 1);
            else
                answers[i] = answer - (int) (Math.random() * 10 - 1);
        }
        String questionText = Integer.toBinaryString(answer) + " в двоичной системе эквивалентно десятичному" +
                "";
        String[] strAnswers = {answers[0]+"", answers[1]+"", answers[2]+"", answers[3]+""};
        question.setQuestion(questionText);
        question.setAnswers(strAnswers);
        question.setRightAnswer(rAnswer);
        return question;
    }

    public static Question generateDecimalToBinary() {
        Question question = new Question();
        question.setType(2);
        int answer =  (int) (Math.random() * 100);
        int[] answers = new int[4];
        int rAnswer = (int) (Math.random() * 4);
        answers[0] = answer;
        for (int i = 0; i < answers.length; i++) {
            if (i == rAnswer)
                answers[i] = answer;
            else if ((int) (Math.random() * 2) == 1)
                answers[i] = answer + (int) (Math.random() * 10 + 1);
            else
                answers[i] = answer - (int) (Math.random() * 10 - 1);
        }
        String questionText = answer + " в десятичной системе эквивалентно двоичному";
        String[] strAnswers = {Integer.toBinaryString(answers[0])+"", Integer.toBinaryString(answers[1])+"",
                Integer.toBinaryString(answers[2])+"", Integer.toBinaryString(answers[3])+""};
        question.setQuestion(questionText);
        question.setAnswers(strAnswers);
        question.setRightAnswer(rAnswer);
        return question;
    }

    public static Question generateHexToDecimal() {
        Question question = new Question();
        question.setType(2);
        int answer =  (int) (Math.random() * 100);
        int[] answers = new int[4];
        int rAnswer = (int) (Math.random() * 4);
        answers[0] = answer;
        for (int i = 0; i < answers.length; i++) {
            if (i == rAnswer)
                answers[i] = answer;
            else if ((int) (Math.random() * 2) == 1)
                answers[i] = answer + (int) (Math.random() * 10 + 1);
            else
                answers[i] = answer - (int) (Math.random() * 10 - 1);
        }
        String questionText = Integer.toHexString(answer) + " в шестнадцатеричной системе эквивалентно десятичному" +
                "";
        String[] strAnswers = {answers[0]+"", answers[1]+"", answers[2]+"", answers[3]+""};
        question.setQuestion(questionText);
        question.setAnswers(strAnswers);
        question.setRightAnswer(rAnswer);
        return question;
    }

    public static Question generateDecimalToHex() {
        Question question = new Question();
        question.setType(2);
        int answer =  (int) (Math.random() * 100);
        int[] answers = new int[4];
        int rAnswer = (int) (Math.random() * 4);
        answers[0] = answer;
        for (int i = 0; i < answers.length; i++) {
            if (i == rAnswer)
                answers[i] = answer;
            else if ((int) (Math.random() * 2) == 1)
                answers[i] = answer + (int) (Math.random() * 10 + 1);
            else
                answers[i] = answer - (int) (Math.random() * 10 - 1);
        }
        String questionText = answer + " в десятичной системе эквивалентно шестнадцатеричному";
        String[] strAnswers = {Integer.toHexString(answers[0])+"", Integer.toHexString(answers[1])+"",
                Integer.toHexString(answers[2])+"", Integer.toHexString(answers[3])+""};
        question.setQuestion(questionText);
        question.setAnswers(strAnswers);
        question.setRightAnswer(rAnswer);
        return question;
    }
}
