package com.example.nas19_trivia;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TriviaRequest {

    private final String scoresUrl = "https://ide50-davidknigge.legacy.cs50.io/list";
    private final String questionsUrl = "https://opentdb.com/api.php?";

    private Context context;

    private QuestionsCallback questionsCallback;
    private ScoreCallback scoreCallback;
    private PostScoreCallback postScoreCallback;

    public TriviaRequest(Context context) {this.context = context;}

    public class onQuestionsResponseListener implements Response.Listener<JSONObject>, Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error) {
            questionsCallback.gotQuestionsError(error.getMessage());
        }
        @Override
        public void onResponse(JSONObject response) {
            try {
                ArrayList<Question> questions = new ArrayList<>();
                JSONArray results = response.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject q = results.getJSONObject(i);

                    ArrayList<String> incorrectAnswers = new ArrayList<>();
                    JSONArray incorrectAnswersArr = q.getJSONArray("incorrect_answers");
                    for (int j = 0; j < incorrectAnswersArr.length(); j++) {
                        incorrectAnswers.add(Html.fromHtml(
                                incorrectAnswersArr.getString(j),
                                Html.FROM_HTML_MODE_LEGACY
                        ).toString());
                    }
                    questions.add(new Question(
                            Html.fromHtml(q.getString("question"), Html.FROM_HTML_MODE_LEGACY).toString(),
                            Html.fromHtml(q.getString("difficulty"), Html.FROM_HTML_MODE_LEGACY).toString(),
                            Html.fromHtml(q.getString("correct_answer"), Html.FROM_HTML_MODE_LEGACY).toString(),
                            incorrectAnswers
                    ));
                }
                questionsCallback.gotQuestions(questions);
            } catch (JSONException e){
                questionsCallback.gotQuestionsError(e.getMessage());
            }
        }
    }

    public class onScoresResponseListener implements Response.Listener<JSONArray>, Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error) {
            scoreCallback.gotScoresError(error.getMessage());
        }
        @Override
        public void onResponse(JSONArray response) {
            try {
                ArrayList<Highscore> highscores = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    highscores.add(new Highscore(
                            response.getJSONObject(i).getString("name"),
                            response.getJSONObject(i).getString("score")
                    ));
                }
                scoreCallback.gotScores(highscores);
            } catch (JSONException e) {
                scoreCallback.gotScoresError(e.getMessage());
            }
        }
    }

    public class onPostScoresResponseListener implements Response.Listener<JSONObject>, Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error) {
            postScoreCallback.postedScoreError(error.getMessage());

        }
        @Override
        public void onResponse(JSONObject response) {
            postScoreCallback.postedScore();
        }
    }

    public interface QuestionsCallback {
        void gotQuestions(ArrayList<Question> questions);
        void gotQuestionsError(String message);
    }

    public interface PostScoreCallback {
        void postedScore();
        void postedScoreError(String message);
    }

    public interface ScoreCallback {
        void gotScores(ArrayList<Highscore> scores);
        void gotScoresError(String message);
    }

    public void getQuestions(QuestionsCallback callback, String specs) {
        this.questionsCallback = callback;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                this.questionsUrl + specs,
                null,
                new onQuestionsResponseListener(),
                new onQuestionsResponseListener()
        );
        queue.add(jsonObjReq);
    }

    public void getScores(ScoreCallback callback) {
        this.scoreCallback = callback;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                this.scoresUrl,
                null,
                new onScoresResponseListener(),
                new onScoresResponseListener()
        );
        queue.add(jsonArrReq);
    }

    public void postScore(PostScoreCallback callback, String score, String name) {
        this.postScoreCallback = callback;
        RequestQueue queue = Volley.newRequestQueue(context);
        Map<String, String> params = new HashMap<>();
        params.put("score", score);
        params.put("name", name);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                this.scoresUrl,
                new JSONObject(params),
                new onPostScoresResponseListener(),
                new onPostScoresResponseListener()
        );
        queue.add(jsonObjReq);
    }
}
