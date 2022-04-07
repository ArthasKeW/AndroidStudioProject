package com.example.assignment;

public class QuestionItem {
    private String question;
    private String answer;

    public QuestionItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
