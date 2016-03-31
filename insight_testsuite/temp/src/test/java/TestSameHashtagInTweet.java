import com.insight.mk.hashtag.Application;
import com.insight.mk.hashtag.DateUtil;
import com.insight.mk.hashtag.Tweet;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by mk on 3/31/16.
 */
public class TestSameHashtagInTweet {

    @Test
    public void testTweet() throws Exception {
        Tweet tweet = new Tweet(DateUtil.getLongTimestamp("Thu Mar 24 17:51:10 +0000 2016"), new LinkedHashSet<>(Arrays.asList("same", "same")));
        Application application = new Application();
        String average = application.processTweet(tweet);
        assertEquals("0.00", average);
    }
}
