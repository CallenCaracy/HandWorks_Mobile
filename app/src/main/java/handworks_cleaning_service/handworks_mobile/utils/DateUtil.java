package handworks_cleaning_service.handworks_mobile.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateUtil {
    public static String formatDateFromIntToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static String formatStringDate(String strDate) {
        OffsetDateTime date = OffsetDateTime.parse(strDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        return date.format(formatter);
    }

    public static long convertStringToLong(String strDate) {
        OffsetDateTime dateTime = OffsetDateTime.parse(strDate);
        return dateTime.toInstant().toEpochMilli();
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

    public static String extractDateFromISO8601TimeStamps(String isoDate){
        OffsetDateTime dateTime = OffsetDateTime.parse(isoDate);
        return dateTime.toLocalDate().toString();
    }

    public static String extractTimeFromISO8601TimeStamps(String isoDate){
        OffsetDateTime dateTime = OffsetDateTime.parse(isoDate);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        return dateTime.format(timeFormatter);
    }

    public static String addExtraHours(String endTime, int extraHours) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Objects.requireNonNull(sdf.parse(endTime)));
        } catch (ParseException e) {
            Log.d("Error", "Failed to add extra hours to end time.");
        }

        calendar.add(Calendar.HOUR_OF_DAY, extraHours);
        return sdf.format(calendar.getTime());
    }

    public static LocalDate extractLocalDate(String isoDate) {
        return OffsetDateTime.parse(isoDate).toLocalDate();
    }

    public static LocalTime extractLocalTime(String isoDate) {
        return OffsetDateTime.parse(isoDate).toLocalTime();
    }
}
