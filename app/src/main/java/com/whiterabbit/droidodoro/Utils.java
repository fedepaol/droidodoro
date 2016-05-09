package com.whiterabbit.droidodoro;

import static java.lang.String.format;

public class Utils {
    public static String getTimeFromSeconds(long seconds) {
        long hours = seconds / (3600);
        long minutes = seconds / 60;
        long sec = seconds % 60;
        return format("%02d:%02d:%02d", hours, minutes, sec);
    }
}
