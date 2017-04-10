package com.fomono.fomono.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Saranu on 4/6/17.
 */

public class DateUtils {

    public static String getFormattedDate(String strDate){
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = utcFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        return pstFormat.format(date);

    }


    public static String getFormattedDateForHeader(String strDate){
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = utcFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat pstFormat = new SimpleDateFormat("MMM dd");
        pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        return pstFormat.format(date);

    }


    public static long convertUTCtoMilliSeconds(String strDate){

        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
