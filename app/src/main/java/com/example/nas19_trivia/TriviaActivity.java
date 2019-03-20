package com.example.nas19_trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TriviaActivity extends AppCompatActivity {

    TriviaGame game;

    /** When an answer is clicked, mark it as answer to the current question, obtain the next
     * question. */
    public class onAnswerClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (game.answerQuestion(((TextView) view).getText().toString())) {
                Toast.makeText(getApplicationContext(), "Correct answer!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect answer...", Toast.LENGTH_LONG).show();
            }

            game.incrementQuestion();
            nextQuestion();
        }
    }

    /** When this activiyt is created, obtain the trivia game instance currently being played. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        game = (TriviaGame) getIntent().getSerializableExtra("game");

        nextQuestion();
    }

    /** If there is a next question, update the TextViews to reflect the current stage in the game
     * If there are no questions left, start the confirmhighscore activity. */
    public void nextQuestion() {
        Question q = game.getNextQuestion();

        if (q != null) {

            TextView question = findViewById(R.id.questionTextView);
            TextView remaining = findViewById(R.id.questionsRemaining);
            TextView correct = findViewById(R.id.scoreTextView);
            ListView answers = findViewById(R.id.answersList);

            question.setText(q.getQuestion());
            remaining.setText("" + game.getQuestionsRemaining() + " questions remaining");
            correct.setText("score: " + game.getScore());
            answers.setAdapter(new ArrayAdapter<String>(this, R.layout.answers_item, q.getAllAnswers()));
            answers.setOnItemClickListener(new onAnswerClickListener());

        } else {

            Intent intent = new Intent(this, ConfirmHighscoreActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);

        }
    }
}
