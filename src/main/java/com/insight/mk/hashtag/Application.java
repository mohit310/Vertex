package com.insight.mk.hashtag;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by mk on 3/30/16.
 *
 * This is the main application which is called by script run.sh
 * @Todo - Cleanup all system outs
 */
public class Application {

    //Default values for input/output file
    private static String inputFile = "tweet_input/tweets.txt";
    private static String outputFile = "tweet_output/output.txt";

    private Graph graph = new Graph();

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            inputFile = args[0];
            outputFile = args[1];
        }
        Application app = new Application();
        app.run();
    }

    private void run() throws Exception {
        List<Tweet> tweets = TweetsParser.getAllTweets(inputFile);
        processTweets(tweets);
    }

    public void processTweets(List<Tweet> tweets) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Tweet tweet : tweets) {
                String average = processTweet(tweet);
                //System.out.println(average);
                writer.write(average);
                writer.newLine();
                writer.flush();
            }
        }
    }

    public String processTweet(Tweet tweet) throws Exception {
        graph.addToGraph(tweet);
        double avg = graph.calculateAverage();
        return String.format("%.2f", avg);
    }


}
