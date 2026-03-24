package handworks_cleaning_service.handworks_mobile.utils;

public class StringUtil {
    public static String capitalizeFirstLetter(String string) {
        if (string != null && !string.isEmpty()) {
            string = string.substring(0, 1).toUpperCase() + string.substring(1);
            return string;
        }
        return "";
    }
}
