#!/usr/bin/env bash

# example of the run script for running the word count

# I'll execute my programs, with the input directory tweet_input and output the files in the directory tweet_output
java -cp ./target/vertex-1.0-SNAPSHOT-jar-with-dependencies.jar com.insight.mk.hashtag.Application "$@"


