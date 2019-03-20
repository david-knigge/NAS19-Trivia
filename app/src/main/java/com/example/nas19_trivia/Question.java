package com.example.nas19_trivia;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

    String question;
    String difficulty;
    String correctAnswer;
    ArrayList<String> incorrectAnswers;

    public Question(String question, String difficulty, String correctAnswer, ArrayList<String> incorrectAnswers) {
        this.question = question;
        this.difficulty = difficulty;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public ArrayList<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public ArrayList<String> getAllAnswers() {
        ArrayList<String> ans = (ArrayList<String>)incorrectAnswers.clone();
        ans.add(correctAnswer);
        return ans;
    }
}
