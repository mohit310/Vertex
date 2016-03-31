import com.insight.mk.hashtag.Application;
import com.insight.mk.hashtag.Tweet;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by mk on 3/31/16.
 */
public class TestBigFileTweets {

    @Test
    public void testTweet() throws Exception {
        Application application = new Application();
        Tweet tweet = new Tweet(1459221792000l, new LinkedHashSet<String>(Arrays.asList("hiring", "PaloAlto", "Healthcare", "Job", "Jobs")));
        String average = application.processTweet(tweet);
        assertEquals("2.00", average);
        tweet = new Tweet(1459221792000l, new LinkedHashSet<String>(Arrays.asList("hiring", "Honolulu", "BusinessMgmt", "Job", "Jobs")));
        average = application.processTweet(tweet);
        assertEquals("2.28", average);
        tweet = new Tweet(1459221792000l, new LinkedHashSet<String>(Arrays.asList("Brentwood", "Hospitality", "Job", "Jobs", "Hiring", "CareerArc")));
        average = application.processTweet(tweet);
        assertEquals("2.36", average);
        tweet = new Tweet(1459221792000l, new LinkedHashSet<String>(Arrays.asList("directvrealmadrid", "FutbolTotalDIRECTV")));
        average = application.processTweet(tweet);
        assertEquals("2.15", average);
    }
}
