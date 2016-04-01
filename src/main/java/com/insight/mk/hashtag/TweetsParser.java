package com.insight.mk.hashtag;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mk on 3/31/16.
 * This class is used to parse Tweeter feed and return a list of tweets based on input file passed
 */
public class TweetsParser {

    /**
     * Static method to parse the tweeter json file and return a list of Tweet objects
     *
     * @param inputFile Tweeter JSON file
     * @return list of tweet objects
     * @throws Exception
     */
    public static List<Tweet> getAllTweets(String inputFile) throws Exception {
        Gson gson = new Gson();
        List<Tweet> tweets = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile), Charset.forName("UTF-8"))) {
            String line = reader.readLine();
            while (line != null) {
                JsonElement root = gson.fromJson(line, JsonElement.class);
                JsonElement createdAtElement = root.getAsJsonObject().get("created_at");
                String createdAt = (createdAtElement != null) ? createdAtElement.getAsString() : null;
                if (createdAt != null) {
                    JsonArray array = root.getAsJsonObject().get("entities").getAsJsonObject().get("hashtags").getAsJsonArray();
                    Iterator<JsonElement> iterator = array.iterator();
                    List<String> hashTags = null;
                    if (array.size() > 0) {
                        hashTags = new ArrayList<>();
                        while (iterator.hasNext()) {
                            JsonElement hashTagElement = iterator.next();
                            String hashTag = hashTagElement.getAsJsonObject().get("text").getAsString();
                            if (!hashTags.contains(hashTag))
                                hashTags.add(hashTag);
                        }
                    }
                    Tweet tweet = new Tweet(DateUtil.getLongTimestamp(createdAt), hashTags);
                    tweets.add(tweet);
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tweets;
    }
}
