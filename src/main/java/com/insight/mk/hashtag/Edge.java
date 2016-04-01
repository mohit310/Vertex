package com.insight.mk.hashtag;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mk on 3/31/16.
 * This class is used to manage the list of all edges from a hashtag
 */
public class Edge {
    private Map<String, Long> edges = new HashMap<>();

    /**
     * Returns the order of the edges
     * @return number of connections
     */
    public int size() {
        return edges.size();
    }

    /**
     * Method to add new edge with timestsamp of tweet
     * @param edge
     * @param timestamp
     */
    public void add(String edge, long timestamp) {
        edges.put(edge, timestamp);
    }

    /**
     * Method to update the edge in case an existing edge exists and change to new timestamp
     * @param edge
     * @param timestamp
     */
    public void update(String edge, long timestamp) {
        edges.replace(edge, timestamp);
    }

    /**
     * Checks if an edge already exists in the list
     * @param edge the edge to check for existence
     * @return true/false value if edge exists
     */
    public boolean contains(String edge) {
        return edges.containsKey(edge);
    }

    public long getCreatedAt(String edge) {
        return (edges.get(edge) != null) ? edges.get(edge) : Long.MIN_VALUE;
    }

    /**
     * Method to remove the edge from the list
     * @param edge
     */
    public void remove(String edge) {
        edges.remove(edge);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Map.Entry<String, Long> entry : edges.entrySet()) {
            builder.append(" " + entry.getKey());
        }
        builder.append("]");
        return builder.toString();
    }
}
