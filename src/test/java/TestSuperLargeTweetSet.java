import com.insight.mk.hashtag.Application;
import com.insight.mk.hashtag.Tweet;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by mk on 4/4/16.
 */
public class TestSuperLargeTweetSet {

    @Test
    public void testLargeList() throws Exception {
        Application application = new Application();
        for (int i = 0; i < 25000; i++) {
            Tweet tweet = new Tweet(new Date().getTime(), Arrays.asList("Test" + i, "Test" + (i + 1)));
            String average = application.processTweet(tweet);
            assertNotNull(average);
        }
    }
}
