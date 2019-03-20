package com.example.nas19_trivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmHighscoreActivity extends AppCompatActivity implements TriviaRequest.PostScoreCallback{

    TriviaGame game;

    /** On creation, retrieve the played game, display the score. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_highscore);

        game = (TriviaGame) getIntent().getSerializableExtra("game");
        TextView answersCorrect = findViewById(R.id.answersCorrect);
        answersCorrect.setText("" + game.getScore());
    }

    /** Return to menu activity. */
    public void tryAgainClicked(View v) {
        finish();
    }

    /** Obtain name and save the score by posting it to the server.*/
    public void saveScoreClicked(View v) {
        TriviaRequest req = new TriviaRequest(this);
        String name = ((EditText) findViewById(R.id.nameView)).getText().toString();

        if (!name.equals("")) {
            req.postScore(this, Integer.toString(game.getScore()), name);
        } else {
            Toast.makeText(this, "Please enter a name!", Toast.LENGTH_LONG).show();
        }
    }

    /** If request succeeds, return to menu activity. */
    @Override
    public void postedScore() {
        finish();
    }

    /** If request fails, display error. */
    @Override
    public void postedScoreError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
