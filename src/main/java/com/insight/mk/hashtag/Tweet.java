package com.insight.mk.hashtag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mk on 3/30/16.
 */
public class Tweet implements Serializable {
    private long createdAt;
    private List<String> hashTags = new ArrayList<>();

    public Tweet(long createdAt, List<String> hashTags) {
        this.createdAt = createdAt;
        if (hashTags != null)
            this.hashTags.addAll(hashTags);
    }

    public void addHashtag(String tag) {
        hashTags.add(tag);
    }

    public long getCreatedAt() {
        return createdAt;
    }

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

        return createdAt == tweet.createdAt;

    }

    @Override
    public int hashCode() {
        return (int) (createdAt ^ (createdAt >>> 32));
    }
}
