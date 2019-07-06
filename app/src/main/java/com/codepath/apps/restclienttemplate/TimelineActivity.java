package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/*** @author - Stanley Nwakamma, Facebook University for Engineering, 2019  ***/

public class TimelineActivity extends AppCompatActivity {

    // these are instance fields:

    TwitterClient client;   //The Twitter Client

    @BindView(R.id.rvTweet) RecyclerView rvTweets;  // Recycler View for Tweets
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    TweetAdapter tweetAdapter;  // Tweet Adapter
    ArrayList<Tweet>tweets;  // Array List that contains all of our Tweets in the feed

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    // Request code for composing activity
    private static final int COMPOSE_ACTIVITY_REQUEST_CODE = 20;
    final String TAG = "TwitterClient";

    public void styleActionBar(){
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        actionBar.setTitle("                   Twitter"); // set the top title
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        //lowest_id = Integer.MAX_VALUE;
        client = TwitterApp.getRestClient(this);
        tweets = new ArrayList<>();

        // Action Bar
        styleActionBar();

        tweetAdapter = new TweetAdapter(tweets);  // construct the adapter from this data source
        // RecyclerView setup (Layout Manager, use adapter)
        LinearLayoutManager ll = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(ll);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(ll) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                populateTimeline();
            }
        };

        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                tweets.clear();
                tweetAdapter.clear();
                populateTimeline();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        rvTweets.setAdapter(tweetAdapter);
        populateTimeline();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Return size of Tweets in feed
    public int getTweetSize() {
        return tweets.size();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the request code is the same and the result code is okay, add to the Tweets array
        if(requestCode == COMPOSE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Unwrap Tweet
            Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
            tweets.add(0,tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miCompose) {
            // Intent to start compose activity
            Intent it = new Intent(TimelineActivity.this, ComposeActivity.class);
            startActivityForResult(it, COMPOSE_ACTIVITY_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateTimeline() {
        long maxId = 0;
        if(tweets.size() > 0) {
            maxId = tweets.get(tweets.size() - 1).uid - 1;
        }

        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //iterate through the JSON array
                // for each entry, deserialize the JSON object

                for (int i = 0; i < response.length(); i++) {

                    try {
                        // convert each object to a tweet model
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        //add that tweet model to our data source
                        tweets.add(tweet);
                        // notify our adapter that we've added an item
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                swipeContainer.setRefreshing(false);
                Log.d("TwitterClient", response.toString());


            }


            @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("TwitterClient", response.toString());

             }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });

    }
}
