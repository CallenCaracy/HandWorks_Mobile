package handworks_cleaning_service.handworks_mobile.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public SharedPreferences provideSharedPrefs(@ApplicationContext Context context) {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new Gson();
    }
}

