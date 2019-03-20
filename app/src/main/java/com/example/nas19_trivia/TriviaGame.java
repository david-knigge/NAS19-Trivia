package com.example.nas19_trivia;

import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

/** Class containing all information on a given game. The list of questions, current question,
 * score, and implements way to answer the current question. */
public class TriviaGame implements Serializable {

    String category, difficulty;
    ArrayList<Question> questions;

    private int current;
    private int score;

    public TriviaGame(String category, String difficulty, ArrayList<Question> questions) {
        this.category = category;
        this.difficulty = difficulty;
        this.questions = questions;
        this.current = 0;
        this.score = 0;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    /** Obtain the next question if there is any questions left. */
    public Question getNextQuestion() {
        if (current < questions.size()) {
            return questions.get(current);
        } else {
            return null;
        }
    }

    /** Increment the index of the question currently being answered. */
    public void incrementQuestion() {
        current += 1;
    }

    /** Check if a given answer is correct, if so add a point. */
    public boolean answerQuestion(String answer) {
        if (answer.equals(questions.get(current).getCorrectAnswer())) {
            score += 1;
            return true;
        } else {
            return false;
        }
    }

    /** Get the number of questions remaining. */
    public int getQuestionsRemaining() {
        return questions.size() - current;
    }

    public int getScore() {
        return score;
    }
}
