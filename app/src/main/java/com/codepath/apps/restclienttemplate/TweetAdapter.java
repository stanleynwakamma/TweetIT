package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/*** @author - Stanley Nwakamma, Facebook University for Engineering, 2019  ***/

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
    //instance fields:
    private List<Tweet> mTweets;
    Context context;

    // pass the tweet array into the constructor
    public TweetAdapter (List<Tweet> tweets) {
        mTweets = tweets;
    }

    // for each row, inflate the layout and cache references into viewHolder
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to position
        Tweet tweet = mTweets.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTime.setText(getRelativeTimeAgo(tweet.createdAt));

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);

        if(tweet.hasEntities==true){
            String entityUrl = tweet.entity.loadURL;
            holder.ivTweetImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(entityUrl).into(holder.ivTweetImage);

        } else{
            holder.ivTweetImage.setVisibility(View.GONE);
        }
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create viewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;  //User's profile image
        public TextView tvUsername;       //Username
        public TextView tvBody;           //Body of Tweet
        public TextView tvTime;           //Tweet Timestamp
        public ImageView ivTweetImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // perform findViewByID lookups

            ivProfileImage = (ImageView) itemView.findViewById(R. id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            ivTweetImage= (ImageView) itemView.findViewById(R.id.entity_tweet);


        }
    }
}
