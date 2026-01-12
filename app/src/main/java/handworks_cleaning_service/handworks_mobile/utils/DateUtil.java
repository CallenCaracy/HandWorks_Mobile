package handworks_cleaning_service.handworks_mobile.utils;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String formatOrderDate(String isoDateString) {
        OffsetDateTime dateTime = OffsetDateTime.parse(isoDateString); // e.g. 2025-05-13T00:29:45+08:00
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"); // e.g. May 13, 2025 at 12:29 AM
        return dateTime.format(formatter);
    }

    public static String formatDateFromIntToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
