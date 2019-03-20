package com.example.nas19_trivia;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements TriviaRequest.QuestionsCallback{

    String difficulty, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** If start is clicked, check to see if category and difficulty are set, start request
     * to obtain questions. */
    public void onClickStart(View v) {
        TriviaRequest req = new TriviaRequest(this);
        String specs = "amount=10";
        if (difficulty != null) {
            specs += "&difficulty=" + difficulty;
        }
        if (category != null) {
            specs += "&category=" + category;
        }
        req.getQuestions(this, specs);
    }

    /** If highscores is clicked, start the highscore activity. */
    public void onClickHighscores(View v) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }

    /** When questions are obtained, create a trivia game, pass it to the trivia activity. */
    @Override
    public void gotQuestions(ArrayList<Question> questions) {
        TriviaGame game = new TriviaGame(difficulty, category, questions);
        Intent intent = new Intent(this, TriviaActivity.class);
        intent.putExtra("game", game);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void gotQuestionsError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /** When a difficulty selection button is pressed, adjust the difficulty, change the
     * background of the buttons. */
    public void onClickDifficulty(View v) {
        ViewGroup vg = (ViewGroup) v.getParent();
        for (int i = 0; i < vg.getChildCount(); i++) {
            TextView tv = (TextView)vg.getChildAt(i);
            tv.setBackgroundColor(Color.parseColor("#d3d3d3"));
        }
        v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        switch (vg.indexOfChild(v)) {
            case 0:
                difficulty = "easy";
                break;
            case 1:
                difficulty = "medium";
                break;
            case 2:
                difficulty = "hard";
                break;
            case 3:
                difficulty = null;
                break;
        }
    }
}
