package com.insight.mk.hashtag;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mk on 3/31/16.
 * This class is used to maintain the graph of the hashtags which is updated on every tweet as well as responsible for checking
 * and removing older entries from graph if more than 60 seconds old.
 */
public class Graph {

    //MINUTE in MILLISECONDS
    private static final long MINUTE = 60 * 1000;

    //This map stores our edges to calculate the degree of the edges
    private Map<String, Edge> hashtagGraph = new HashMap<>();

    //This list stores all our tweets in the order we receive it. This is used to cleanup the unwanted edges for old tweets
    private List<Tweet> timeSeries = new ArrayList<>();

    //This is the timestamp which is managed to check if 1 minute has elapsed when streaming tweets
    private long maxTimestamp = Long.MIN_VALUE;

    /**
     * This method calculates the average of total degree based on current graph tree
     * @return degree average
     */
    public double calculateAverage() {
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

    /**
     * This method takes a new Tweet and updates the graph edges based on connectivity
     * It compares the maxtimestamp with created_at timestamp from new tweet and decides if it needs to delete old records
     * @param tweet
     */
    public void addToGraph(Tweet tweet) {
        System.out.println(tweet);
        long createdAt = tweet.getCreatedAt();
        maxTimestamp = (maxTimestamp == Long.MIN_VALUE) ? createdAt : maxTimestamp;
        if (createdAt < (maxTimestamp - MINUTE)) return;
        if ((createdAt - maxTimestamp) > MINUTE) {
            deleteBefore60Seconds(createdAt);
            maxTimestamp = createdAt;
        }
        List<String> tags = tweet.getHashTags();
        if (tags != null && tags.size() > 1) {
            timeSeries.add(tweet);
            for (int i = 0; i < tags.size(); i++) {
                int next = ((i + 1) == tags.size()) ? 0 : (i + 1);
                String vertex1 = tags.get(i);
                String vertex2 = tags.get(next);
                addOrUpdate(createdAt, vertex1, vertex2);
                addOrUpdate(createdAt, vertex2, vertex1);
            }
        }
    }
    //Convenience method to update edges for new tweet
    private void addOrUpdate(long createdAt, String vertex1, String vertex2) {
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
    }

    //Convenience method to delete all records from timeseries list and remove unwanted edges from graphs
    private void deleteBefore60Seconds(long createdAt) {
        long createdAtMinus60 = createdAt - MINUTE;
        for (Iterator<Tweet> it = timeSeries.iterator(); it.hasNext(); ) {
            Tweet timeSeriesTweet = it.next();
            long createdAtFromTimeSeries = timeSeriesTweet.getCreatedAt();
            List<String> tags = timeSeriesTweet.getHashTags();
            if (createdAtFromTimeSeries < createdAtMinus60) {
                it.remove();
                if (tags.size() > 1) {
                    for (int i = 0; i < tags.size(); i++) {
                        int next = ((i + 1) == tags.size()) ? 0 : (i + 1);
                        String vertex1 = tags.get(i);
                        String vertex2 = tags.get(next);
                        removeEdge(createdAtFromTimeSeries, vertex1, vertex2);
                        removeEdge(createdAtFromTimeSeries, vertex2, vertex1);
                    }
                }
            }
        }
    }

    //Convenience method to remove edge. Workds in conjunction from above method.
    private void removeEdge(long createdAt, String vertex1, String vertex2) {
        Edge edge = hashtagGraph.get(vertex1);
        if (edge != null) {
            if (edge.getCreatedAt(vertex2) == createdAt)
                edge.remove(vertex2);
            if (edge.size() == 0) hashtagGraph.remove(vertex1);
        }
    }

}
