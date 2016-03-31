package com.insight.mk.hashtag;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by mk on 3/30/16.
 */
public class Application {

    private static final long MINUTE = 60 * 1000;

    private static final String inputFile = "tweet_input/tweets_large.txt";
    private static final String outputFile = "tweet_output/output.txt";

    private Map<String, Edge> hashtagGraph = new HashMap<>();
    private Map<Long, String[]> timeSeries = new TreeMap<>();
    private long maxTimestamp;

    public static void main(String[] args) throws Exception {
        Application app = new Application();
        app.run();
    }

    private void run() throws Exception {
        List<Tweet> tweets = getAllTweets();
        processTweets(tweets);
    }

    public void processTweets(List<Tweet> tweets) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (Tweet tweet : tweets) {
            String average = processTweet(tweet);
            writer.write(average);
            writer.newLine();
            writer.flush();
        }
        writer.close();
    }

    public String processTweet(Tweet tweet) throws Exception {
        updateGraph(tweet);
        double avg = calculateAverage();
        return String.format("%.2f", avg);
    }

    private void updateGraph(Tweet tweet) throws Exception {
        System.out.println(tweet);
        long createdAt = tweet.getCreatedAt();
        if (createdAt < (maxTimestamp - MINUTE)) return;
        maxTimestamp = (maxTimestamp == 0) ? createdAt : maxTimestamp;
        if ((createdAt - maxTimestamp) > MINUTE) {
            deleteBefore(createdAt);
            maxTimestamp = createdAt;
        }
        Set<String> tagSet = tweet.getHashTags();
        String[] tags = null;
        if (tagSet.size() > 0) {
            tags = new String[tagSet.size()];
            tagSet.toArray(tags);
        }
        if (tags != null && tags.length > 1) {
            timeSeries.put(createdAt, tags);
            for (int i = 0; i < tags.length; i++) {
                String vertex1 = tags[i];
                int next = ((i + 1) == tags.length) ? 0 : (i + 1);
                String vertex2 = tags[next];
                Edge edge = hashtagGraph.get(vertex1);
                if (edge == null) {
                    edge = new Edge();
                    hashtagGraph.put(vertex1, edge);
                }
                if (!edge.contains(vertex2)) {
                    edge.add(vertex2, createdAt);
                } else {
                    edge.update(vertex2, createdAt);
                }

                edge = hashtagGraph.get(vertex2);
                if (edge == null) {
                    edge = new Edge();
                    hashtagGraph.put(vertex2, edge);
                }
                if (!edge.contains(vertex1)) {
                    edge.add(vertex1, createdAt);
                } else {
                    edge.update(vertex1, createdAt);
                }
            }
        }
    }

    private void deleteBefore(long createdAt) {
        long createdAtMinus60 = createdAt - MINUTE;
        Set<Map.Entry<Long, String[]>> timeData = timeSeries.entrySet();
        List<Long> toRemove = new ArrayList<>();
        for (Map.Entry<Long, String[]> entry : timeData) {
            long createdAtFromTimeSeries = entry.getKey();
            if (createdAtFromTimeSeries < createdAtMinus60) {
                toRemove.add(createdAtFromTimeSeries);
                String[] tags = entry.getValue();
                if (tags.length > 1) {
                    for (int i = 0; i < tags.length; i++) {
                        String vertex1 = tags[i];
                        int next = ((i + 1) == tags.length) ? 0 : (i + 1);
                        String vertex2 = tags[next];
                        Edge edge = hashtagGraph.get(vertex1);
                        if (edge != null) {
                            if (edge.getCreatedAt(vertex2) == createdAtFromTimeSeries)
                                edge.remove(vertex2);
                            if (edge.size() == 0) hashtagGraph.remove(vertex1);
                        }
                        edge = hashtagGraph.get(vertex2);
                        if (edge != null) {
                            if (edge.getCreatedAt(vertex1) == createdAtFromTimeSeries)
                                edge.remove(vertex1);
                            if (edge.size() == 0) hashtagGraph.remove(vertex2);
                        }

                    }
                }
            }
        }
        for (Long removeKey : toRemove) {
            timeSeries.remove(removeKey);
        }
    }

    public double calculateAverage() throws Exception {
        Set<Map.Entry<String, Edge>> entrySet = hashtagGraph.entrySet();
        int totalNodes = entrySet.size();
        int totalDegree = 0;
        for (Map.Entry<String, Edge> entry : entrySet) {
            Edge edge = entry.getValue();
            totalDegree += edge.size();
        }
        if (totalNodes > 0) {
            double avg = (totalDegree / (double) totalNodes);
            BigDecimal decimal = new BigDecimal(avg);
            BigDecimal roundOff = decimal.setScale(2, BigDecimal.ROUND_DOWN);
            return roundOff.doubleValue();
        }
        return 0.00;
    }

    private List<Tweet> getAllTweets() throws Exception {
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
                    Set<String> hashTags = null;
                    if (array.size() > 0) {
                        hashTags = new LinkedHashSet<>();
                        while (iterator.hasNext()) {
                            JsonElement hashTagElement = iterator.next();
                            hashTags.add(hashTagElement.getAsJsonObject().get("text").getAsString());
                        }
                    }
                    Tweet tweet = new Tweet(DateUtil.getLongTimestamp(createdAt), hashTags);
                    tweets.add(tweet);
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return tweets;
    }
}
