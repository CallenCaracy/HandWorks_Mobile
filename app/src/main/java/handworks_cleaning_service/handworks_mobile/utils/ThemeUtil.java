package handworks_cleaning_service.handworks_mobile.utils;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.THEME_DARK;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.THEME_LIGHT;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.THEME_SYSTEM;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtil {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME = "theme_mode";

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
}