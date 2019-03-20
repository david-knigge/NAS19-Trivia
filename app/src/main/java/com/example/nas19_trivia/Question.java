package com.example.nas19_trivia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/** Stores the question, it's difficulty, and all its answers. */
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

    /** Get a string containing the question. */
    public String getQuestion() {
        return question;
    }

    public String getDifficulty() {
        return difficulty;
    }

    /** Obtains the correct answer for this question. */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public ArrayList<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    /** Returns an arraylist of all answers to a question, shuffle list before returning. */
    public ArrayList<String> getAllAnswers() {
        ArrayList<String> ans = (ArrayList<String>)incorrectAnswers.clone();
        ans.add(correctAnswer);
        Collections.shuffle(ans);
        return ans;
    }
}
