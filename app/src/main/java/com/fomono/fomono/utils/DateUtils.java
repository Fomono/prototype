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


    public static String getFormattedMonthForHeader(String strDate){
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = utcFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat pstFormat = new SimpleDateFormat("MMM");

        return pstFormat.format(date).toUpperCase();

    }

    public static String getFormattedDayForHeader(String strDate){
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = utcFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat pstFormat = new SimpleDateFormat("dd");

        return pstFormat.format(date).toUpperCase();

    }


    public static String getFormattedDateForHeader(String strDate){
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = utcFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat pstFormat = new SimpleDateFormat("MMM" + "\n" + "dd" +
                "\n");

        return pstFormat.format(date).toUpperCase();

    }
    public static long convertMovieDatetoMilliSeconds(String strDate){

        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    public static long convertEventDatetoMilliSeconds(String strDate){

        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    public static String convertEventDatetoDisplayFormat(String strDate){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");


        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdfDisplay.format(date);
    }

    public static String convertMilitarytoStandard(String mTime){
        DateFormat militaryFormat = new SimpleDateFormat( "HHmm");
        DateFormat standardFormat = new SimpleDateFormat( "hh:mm aa");
        Date date = null;
        try {
            date = militaryFormat.parse(mTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return standardFormat.format(date);
    }

    public static String convertEventDateListItemDisplayFormat(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("EEE, MMM dd, hh:mm a");

        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdfDisplay.format(date);
    }
}
