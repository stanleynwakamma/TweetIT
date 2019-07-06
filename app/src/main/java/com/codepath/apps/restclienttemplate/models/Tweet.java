package com.codepath.apps.restclienttemplate.models;

import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * @author - Stanley Nwakamma, Facebook University for ENgineering; 2019.
 * Timeline for Twitter
 **/
@Parcel  // Annotation indicate class is Parcelable
public class Tweet implements Parcelable {
    // Attributes:
    public String body;
    public  long uid;  // database ID for the tweet
    public String createdAt;
    public User user;
    public Entity entity;
    public boolean hasEntities;

    // Default Constructor
    public Tweet() { }

    protected Tweet(android.os.Parcel in) {
        body = in.readString();
        uid = in.readLong();
        createdAt = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(android.os.Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };


    public static Tweet fromJSON(JSONObject obj) throws JSONException {
        Tweet tweet = new Tweet();
        // Extract the values from the JSON
        tweet.body = obj.getString("text");
        tweet.uid = obj.getLong("id");
        tweet.createdAt = obj.getString("created_at");
        tweet.user = User.fromJSON(obj.getJSONObject("user"));

        JSONObject entityObject = obj.getJSONObject("entities");
        if(entityObject.has("media")){
            JSONArray mediaEndpoint = entityObject.getJSONArray("media");
            if(mediaEndpoint!=null && mediaEndpoint.length()!=0){
                tweet.entity = Entity.fromJSON(obj.getJSONObject("entities"));
                tweet.hasEntities = true;
            }
        }
        //tweet.entity = Entity.fromJSON()
        return tweet;
    }

    // Return size of body
    @Override
    public int describeContents() {
        return body.length();
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeLong(uid);
        dest.writeString(createdAt);
        dest.writeParcelable(user, flags);
    }
}