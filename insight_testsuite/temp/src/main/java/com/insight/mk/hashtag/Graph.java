package com.insight.mk.hashtag;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mk on 3/31/16.
 */
public class Graph {

    private static final long MINUTE = 60 * 1000;

    private Map<String, Edge> hashtagGraph = new HashMap<>();
    private Map<Long, List<String>> timeSeries = new LinkedHashMap<>();
    private long maxTimestamp = Long.MIN_VALUE;

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

    public void addToGraph(Tweet tweet) throws Exception {
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
            timeSeries.put(createdAt, tags);
            for (int i = 0; i < tags.size(); i++) {
                int next = ((i + 1) == tags.size()) ? 0 : (i + 1);
                String vertex1 = tags.get(i);
                String vertex2 = tags.get(next);
                addOrUpdate(createdAt, vertex1, vertex2);
                addOrUpdate(createdAt, vertex2, vertex1);
            }
        }
    }

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

    private void deleteBefore60Seconds(long createdAt) {
        long createdAtMinus60 = createdAt - MINUTE;
        for (Iterator<Map.Entry<Long, List<String>>> it = timeSeries.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Long, List<String>> timeSeriesData = it.next();
            long createdAtFromTimeSeries = timeSeriesData.getKey();
            List<String> tags = timeSeriesData.getValue();
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

    private void removeEdge(long createdAt, String vertex1, String vertex2) {
        Edge edge = hashtagGraph.get(vertex1);
        if (edge != null) {
            if (edge.getCreatedAt(vertex2) == createdAt)
                edge.remove(vertex2);
            if (edge.size() == 0) hashtagGraph.remove(vertex1);
        }
    }

}
