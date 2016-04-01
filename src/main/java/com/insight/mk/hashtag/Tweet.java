package com.insight.mk.hashtag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mk on 3/30/16.
 */
public class Tweet {

    private long createdAt;
    private List<String> hashTags = new ArrayList<>();

    /**
     * Constructor to create a new Tweet. Once constructed the tweet object is unmodifiable
     * @param createdAt
     * @param hashTags
     */
    public Tweet(long createdAt, List<String> hashTags) {
        this.createdAt = createdAt;
        if (hashTags != null)
            this.hashTags.addAll(hashTags);
    }

    /**
     * Method to add a new hashtag to existing list
     * @param tag
     */
    public void addHashtag(String tag) {
        hashTags.add(tag);
    }

    /**
     * returns timestamp of creation
     * @return
     */
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * returns a copy of all hashtags
     * @return
     */
    public List<String> getHashTags() {
        return Collections.unmodifiableList(hashTags);
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "createdAt='" + createdAt + '\'' +
                ", hashTags=" + hashTags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tweet tweet = (Tweet) o;

        if (createdAt != tweet.createdAt) return false;
        return checkifTagsSame(tweet);

    }

    private boolean checkifTagsSame(Tweet otherTweet) {
        List<String> otherTags = otherTweet.getHashTags();
        if (otherTags == null && hashTags == null) return true;
        if (otherTags != null && hashTags == null) return false;
        if (otherTags != null && hashTags != null) {
            if(otherTags.size() != hashTags.size()) return false;
            for (String otherHashtag : otherTags) {
                if (!hashTags.contains(otherHashtag)) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (createdAt ^ (createdAt >>> 32));
        result = 31 * result + (hashTags != null ? hashTags.hashCode() : 0);
        return result;
    }
}
