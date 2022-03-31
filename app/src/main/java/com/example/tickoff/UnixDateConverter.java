package com.example.tickoff;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UnixDateConverter {
    public static long toUnixTime(long dateInLong){
        return dateInLong / 1000;
    }

    public static String toDateString(long unixTimestamp){
        Date dateRaw = new Date(unixTimestamp * 1000);
        SimpleDateFormat dateFormatted = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormatted.format(dateRaw);
    }
}

