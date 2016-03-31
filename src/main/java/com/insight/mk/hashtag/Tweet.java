package com.insight.mk.hashtag;

import java.io.Serializable;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * Created by mk on 3/30/16.
 */
public class Tweet implements Serializable {
    private long createdAt;
    private Set<String> hashTags = new LinkedHashSet<>();

    public Tweet(long createdAt, Set<String> hashTags) {
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

    public Set<String> getHashTags() {
        return hashTags;
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
