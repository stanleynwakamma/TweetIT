package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.wafflecopter.charcounttextview.CharCountTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/*** @author - Stanley Nwakamma, Facebook University for Engineering, 2019  ***/

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;
    @BindView(R.id.etNewTweet) EditText etNewTweet;
    @BindView(R.id.btnSubmit) Button btnSubmit;
    @BindView(R.id.tvTextCounter) CharCountTextView tvCharacterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);
        styleActionBar();
        // Set Overall Layout Background

        ButterKnife.bind(this);
        tvCharacterCount.setEditText(etNewTweet);  // For displaying text limit
        tvCharacterCount.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int i, boolean b) {
            }
        });



    }

    public void styleActionBar() {
        ActionBar actionBar = getSupportActionBar();

    }


    @OnClick(R.id.btnSubmit)
    public void submitTweet() {
        client.sendTweet(etNewTweet.getText().toString(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                try {
                    Tweet tweet = Tweet.fromJSON(obj);
                    Intent it = new Intent(ComposeActivity.this, TimelineActivity.class);
                    it.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    setResult(RESULT_OK, it);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                throwable.printStackTrace();
            }


        });
    }
}



