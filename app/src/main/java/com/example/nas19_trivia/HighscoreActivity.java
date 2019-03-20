package com.example.nas19_trivia;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity implements TriviaRequest.ScoreCallback {

    /** Arrayadapter for listing all highscores retrieved from server. */
    public class HighscoreAdapter extends ArrayAdapter<Highscore> {

        ArrayList<Highscore> scores;

        /** Set name and score in ListView entry. */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.highscore_item, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.highscoreName)).setText(scores.get(position).getName());
            ((TextView) convertView.findViewById(R.id.highscoreScore)).setText(scores.get(position).getScore());
            return convertView;
        }

        public HighscoreAdapter(Context context, int resource, ArrayList<Highscore> objects) {
            super(context, resource, objects);
            scores = objects;
        }
    }

    /** On creation, obtain highscores by making a get request to flask server. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        TriviaRequest req = new TriviaRequest(this);
        req.getScores(this);
    }

    /** If scores are succesfully retrieved, set the adapter to display them in ListView. */
    @Override
    public void gotScores(ArrayList<Highscore> scores) {
        ((ListView) findViewById(R.id.highscoreList)).setAdapter(new HighscoreAdapter(
                this,
                R.layout.highscore_item,
                scores
        ));
    }

    @Override
    public void gotScoresError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
