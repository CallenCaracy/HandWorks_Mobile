package handworks_cleaning_service.handworks_mobile.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String formatDateFromIntToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static String getTimeAgo(long timeMillis) {
        long now = System.currentTimeMillis();

        if (timeMillis < 1_000_000_000_000L) {
            timeMillis *= 1000;
        }

        long diff = now - timeMillis;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = days / 365;

        if (years > 0) return years + " year" + (years > 1 ? "s" : "") + " ago";
        if (months > 0) return months + " month" + (months > 1 ? "s" : "") + " ago";
        if (days > 0) return days + " day" + (days > 1 ? "s" : "") + " ago";
        if (hours > 0) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        if (minutes > 0) return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";

        return "Just now";
    }
}
