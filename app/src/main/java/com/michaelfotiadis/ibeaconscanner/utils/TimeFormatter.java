package com.michaelfotiadis.ibeaconscanner.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeFormatter {
    private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";
    private static final SimpleDateFormat ISO_FORMATTER = new UtcDateFormatter(ISO_FORMAT, Locale.US);

    public static String getIsoDateTime(Date date) {
        return ISO_FORMATTER.format(date);
    }

    public static String getIsoDateTime(long j) {
        return getIsoDateTime(new Date(j));
    }
}
