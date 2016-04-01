#!/usr/bin/env bash

# Compile code
mvn package

# I'll execute my programs, with the input directory tweet_input and output the files in the directory tweet_output
java -Xmx2048m -cp ./target/vertex-1.0-SNAPSHOT-jar-with-dependencies.jar com.insight.mk.hashtag.Application ./tweet_input/tweets.txt ./tweet_output/output.txt


