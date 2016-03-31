package com.insight.mk.hashtag;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mk on 3/31/16.
 */
public class Edge {
    private Map<String, Long> edges = new HashMap<>();

    public int size() {
        return edges.size();
    }

    public void add(String edge, long timestamp) {
        edges.put(edge, timestamp);
    }

    public void update(String edge, long timestamp) {
        edges.replace(edge, timestamp);
    }

    public boolean contains(String edge) {
        return edges.containsKey(edge);
    }

    public long getCreatedAt(String edge) {
        return (edges.get(edge) != null) ? edges.get(edge) : Long.MIN_VALUE;
    }

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
