package handworks_cleaning_service.handworks_mobile;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;
import handworks_cleaning_service.handworks_mobile.utils.ThemeUtil;

import com.clerk.api.Clerk;

@HiltAndroidApp
public class HandworksMobileApp extends Application {

    @Override
    public void onCreate() {
        ThemeUtil.applyTheme(this);
        super.onCreate();

        Clerk.INSTANCE.initialize(
                this,
                "pk_test_dXB3YXJkLWdlY2tvLTc1LmNsZXJrLmFjY291bnRzLmRldiQ"
        );
    }
}