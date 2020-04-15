package br.com.eike.tempmute.utils;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class TimeUtils {

    private TimeUtils() {}

    private static final Map<Character, Long> TIME_VALUES = ImmutableMap.of(
            's', 1000L,
            'm', 60000L,
            'h', 3600000L,
            'd', 86400000L,
            'w', 604800000L
    );

    public static long millisFromString(String time) {
        int timeLenght = time.length();
        char last = time.charAt(timeLenght - 1);

        String toParse = time;
        Long multiplier = 1000L;

        if (Character.isLetter(last)) {
            toParse = time.substring(0, timeLenght - 1);
            multiplier = TIME_VALUES.get(last);
            if (multiplier == null) return -1;
        }

        int timeInt;

        try {
            timeInt = Integer.parseInt(toParse);
        } catch (Exception $) {
            return -1;
        }

        return timeInt * multiplier;
    }

    public static String format(long time) {
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 24;
        long day = (time / (1000 * 60 * 60 * 24)) % 7;
        long week = (time / (1000 * 60 * 60 * 24 * 7)) % 30;

        StringBuilder builder = new StringBuilder();

        if(week > 0) {
            builder.append(week).append(" ").append(week == 1 ? "week" : "weeks").append(" ");
        }

        if(day > 0) {
            builder.append(day).append(" ").append(day == 1 ? "day" : "days").append(" ");
        }

        if(hour > 0) {
            builder.append(hour).append(" ").append(hour == 1 ? "hour" : "hours").append(" ");
        }

        if(minute > 0) {
            builder.append(minute).append(" ").append(minute == 1 ? "minute" : "minutes").append(" ");
        }

        if(second > 0) {
            builder.append(second).append(" ").append(second == 1 ? "second" : "seconds").append(" ");
        }

        return builder.toString().trim();
    }

}
