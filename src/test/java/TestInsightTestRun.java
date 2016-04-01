import com.insight.mk.hashtag.Application;
import com.insight.mk.hashtag.Tweet;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by mk on 3/31/16.
 */
public class TestInsightTestRun {


    @Test
    public void testAverage() throws Exception {
        Application application = new Application();
        Tweet tweet = new Tweet(1446717939000l, Arrays.asList("Pisteare", "elsientometro"));
        String average = application.processTweet(tweet);
        assertEquals("1.00", average);
        tweet = new Tweet(1446717939000l, Arrays.asList("fullset", "whitepinkacrylic", "ArceliasNails", "mobilehomebasednailservice"));
        average = application.processTweet(tweet);
        assertEquals("1.66", average);
    }


}
