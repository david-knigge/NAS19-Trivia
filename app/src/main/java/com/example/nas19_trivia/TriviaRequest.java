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

/** Class containing all requests for this application along with their callback interfaces. */
public class TriviaRequest {

    private final String scoresUrl = "https://ide50-davidknigge.legacy.cs50.io/list";
    private final String questionsUrl = "https://opentdb.com/api.php?";

    private Context context;

    private QuestionsCallback questionsCallback;
    private ScoreCallback scoreCallback;
    private PostScoreCallback postScoreCallback;

    public TriviaRequest(Context context) {this.context = context;}

    /** Interface for request to obtain questions. */
    public interface QuestionsCallback {
        void gotQuestions(ArrayList<Question> questions);
        void gotQuestionsError(String message);
    }

    /** Interface for request to post highscore. */
    public interface PostScoreCallback {
        void postedScore();
        void postedScoreError(String message);
    }

    /** Interface for request to obtain a list of all highscores. */
    public interface ScoreCallback {
        void gotScores(ArrayList<Highscore> scores);
        void gotScoresError(String message);
    }

    /** Responselistener for request to obtain trivia questions. */
    public class onQuestionsResponseListener implements Response.Listener<JSONObject>, Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            questionsCallback.gotQuestionsError(error.getMessage());
        }

        /** If a successful response is returned parse the questions and pass them back through
         * callback.*/
        @Override
        public void onResponse(JSONObject response) {
            try {
                ArrayList<Question> questions = new ArrayList<>();
                JSONArray results = response.getJSONArray("results");

                // Run through list of questions
                for (int i = 0; i < results.length(); i++) {
                    JSONObject q = results.getJSONObject(i);

                    // Create an arraylist containing the incorrect answers.
                    ArrayList<String> incorrectAnswers = new ArrayList<>();
                    JSONArray incorrectAnswersArr = q.getJSONArray("incorrect_answers");
                    for (int j = 0; j < incorrectAnswersArr.length(); j++) {
                        incorrectAnswers.add(Html.fromHtml(
                                incorrectAnswersArr.getString(j),
                                Html.FROM_HTML_MODE_LEGACY
                        ).toString());
                    }

                    // Create the question object
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

    /** Response listener for request to obtain all highscores. */
    public class onScoresResponseListener implements Response.Listener<JSONArray>, Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            scoreCallback.gotScoresError(error.getMessage());
        }

        /** If a successful response is returned, add all highscores to an arraylist and return
         * it through the callback. */
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

    /** Responselistener for request to post highscore. */
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

    /** Create a request to obtain questions from opentdb.
     * specs -- URL formatted string containing GET parameters. */
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

    /** Create a GET request to obtain all highscores. */
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

    /** Create a POST request to save a high score. Had to add a line of code to the flask
     * server to have it accept JSON data. */
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
