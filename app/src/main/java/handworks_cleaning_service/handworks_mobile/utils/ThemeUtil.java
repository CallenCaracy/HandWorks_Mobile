package handworks_cleaning_service.handworks_mobile.utils;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.KEY_THEME;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.PREFS_NAME;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.THEME_DARK;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.THEME_LIGHT;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.THEME_SYSTEM;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import handworks_cleaning_service.handworks_mobile.ui.models.ThemeOption;

public class ThemeUtil {
    public static void applyTheme(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        int themeMode = prefs.getInt(KEY_THEME, THEME_SYSTEM);

        switch (themeMode) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
                break;

            case THEME_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    public static void setTheme(Context context, int themeMode) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_THEME, themeMode)
                .apply();

        applyTheme(context);
    }

    public static int getTheme(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME, THEME_SYSTEM);
    }

    public static boolean themeMatchesCurrent(ThemeOption theme, int currentTheme) {
        return (theme.name.equals("System") && currentTheme == Constant.THEME_SYSTEM)
                || (theme.name.equals("Light") && currentTheme == Constant.THEME_LIGHT)
                || (theme.name.equals("Dark") && currentTheme == Constant.THEME_DARK);
    }
}