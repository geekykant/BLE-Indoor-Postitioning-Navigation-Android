package com.michaelfotiadis.ibeaconscanner.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class UtcDateFormatter extends SimpleDateFormat {
    private static final String TIME_ZONE_STRING = "UTC";
    private static final TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone(TIME_ZONE_STRING);
    private static final long serialVersionUID = 1;

    public UtcDateFormatter(String str) {
        super(str);
        super.setTimeZone(TIME_ZONE_UTC);
    }

    public UtcDateFormatter(String str, DateFormatSymbols dateFormatSymbols) {
        super(str, dateFormatSymbols);
        super.setTimeZone(TIME_ZONE_UTC);
    }

    public UtcDateFormatter(String str, Locale locale) {
        super(str, locale);
        super.setTimeZone(TIME_ZONE_UTC);
    }

    public void setTimeZone(TimeZone timeZone) {
        throw new UnsupportedOperationException("This SimpleDateFormat can only be in UTC");
    }
}
