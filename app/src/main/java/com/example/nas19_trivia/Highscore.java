package com.example.nas19_trivia;

import java.io.Serializable;

public class Highscore implements Serializable {

    String name, score;

    public Highscore(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }
}
