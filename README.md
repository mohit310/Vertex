Insight Data Engineering - Coding Challenge By @mohit310
===========================================================

## Steps to compile
In order to compile the program please run "mvn package" so that jar with all dependencies is created under target directory.
The run.sh script will use the jar file with all dependencies

## run.sh script
Make sure you have apache-maven installed on your computer and the path set so that _mvn_ executable can be run.
The script will first compile and package code and make it available so that we can run the program

Then input tweet file is hardcoded in script to "**./tweet_input/tweets.txt**"

Then output average file is hardcoded in script to "**./tweet_output/output.txt**"

You can change the input and output directory and file in run.sh script


## Project Dependencies
apache-maven - build tool

GSON - Google JSON parser library

Junit - unit testing code

