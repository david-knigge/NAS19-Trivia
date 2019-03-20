package com.example.nas19_trivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class confirmHighscoreActivity extends AppCompatActivity implements TriviaRequest.PostScoreCallback{

    TriviaGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_highscore);

        game = (TriviaGame) getIntent().getSerializableExtra("game");
        TextView answersCorrect = findViewById(R.id.answersCorrect);
        answersCorrect.setText("" + game.getScore());
    }

    public void tryAgainClicked(View v) {
        finish();
    }

    public void saveScoreClicked(View v) {
        TriviaRequest req = new TriviaRequest(this);
        String name = ((EditText) findViewById(R.id.nameView)).getText().toString();

        if (name != null) {
            req.postScore(this, Integer.toString(game.getScore()), name);
        }
    }

    @Override
    public void postedScore() {
        finish();
    }

    @Override
    public void postedScoreError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
