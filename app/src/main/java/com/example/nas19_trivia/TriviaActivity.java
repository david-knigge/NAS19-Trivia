package com.example.nas19_trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TriviaActivity extends AppCompatActivity {

    TriviaGame game;

    public class onAnswerClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            game.answerQuestion(((TextView) view).getText().toString());
            game.incrementQuestion();
            nextQuestion();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        game = (TriviaGame) getIntent().getSerializableExtra("game");

        nextQuestion();
    }

    public void nextQuestion() {
        Question q = game.getNextQuestion();

        if (q != null) {

            TextView question = findViewById(R.id.questionTextView);
            TextView remaining = findViewById(R.id.questionsRemaining);
            ListView answers = findViewById(R.id.answersList);

            question.setText(q.getQuestion());
            remaining.setText("" + game.getQuestionsRemaining() + " questions remaining");
            answers.setAdapter(new ArrayAdapter<String>(this, R.layout.answers_item, q.getAllAnswers()));
            answers.setOnItemClickListener(new onAnswerClickListener());

        } else {

            Intent intent = new Intent(this, confirmHighscoreActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);

        }
    }
}
