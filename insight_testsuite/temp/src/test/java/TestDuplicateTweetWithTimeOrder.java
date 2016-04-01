import com.insight.mk.hashtag.Application;
import com.insight.mk.hashtag.DateUtil;
import com.insight.mk.hashtag.Tweet;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by mk on 3/31/16.
 */
public class TestDuplicateTweetWithTimeOrder {

    @Test
    public void testTweet() throws Exception {
        Application application = new Application();
        Tweet tweet = new Tweet(DateUtil.getLongTimestamp("Thu Mar 24 17:51:10 +0000 2016"), Arrays.asList("Spark", "Apache"));
        String average = application.processTweet(tweet);
        assertEquals("1.00", average);
        tweet = new Tweet(DateUtil.getLongTimestamp("Thu Mar 24 17:51:55 +0000 2016"), Arrays.asList("Spark", "Apache"));
        average = application.processTweet(tweet);
        assertEquals("1.00", average);
        tweet = new Tweet(DateUtil.getLongTimestamp("Thu Mar 24 17:52:15 +0000 2016"), Arrays.asList("Apache", "Cassandra"));
        average = application.processTweet(tweet);
        assertEquals("1.33", average);
    }

}
