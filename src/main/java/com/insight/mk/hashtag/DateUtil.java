package com.insight.mk.hashtag;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mk on 3/31/16.
 */
public class DateUtil {

    /**
     * Static method to convert string value of timestamp into long value based on date format "EEE MMM dd HH:mm:ss +SSS yyyy"
     * @param createdAt - input date-timestamp string
     * @return the long value of date-timestamp
     * @throws Exception
     */
    public static final long getLongTimestamp(String createdAt) throws Exception {
        //Thu Mar 24 17:51:10 +0000 2016
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss +SSS yyyy");
        Date date = dateFormat.parse(createdAt);
        return date.getTime();
    }
}
