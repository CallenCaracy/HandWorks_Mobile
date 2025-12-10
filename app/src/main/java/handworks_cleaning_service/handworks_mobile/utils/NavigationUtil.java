package handworks_cleaning_service.handworks_mobile.utils;

import android.app.Activity;
import android.content.Intent;

public class NavigationUtil {

    public static void navigateTo(Activity currentActivity, Class<?> targetActivity) {
        Intent intent = new Intent(currentActivity, targetActivity);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }
}

