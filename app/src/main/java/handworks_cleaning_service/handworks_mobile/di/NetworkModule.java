package handworks_cleaning_service.handworks_mobile.di;

import android.util.Log;

import com.clerk.api.Clerk;
import com.clerk.api.session.Session;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

import handworks_cleaning_service.handworks_mobile.BuildConfig;
import handworks_cleaning_service.handworks_mobile.data.remote.BookApi;
import handworks_cleaning_service.handworks_mobile.data.remote.TaskApi;
import handworks_cleaning_service.handworks_mobile.data.remote.UserApi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Log.d("HTTP", message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();

//                    Keep this just in case (Legacy)
//                    Session session = authRepository.getSession();
//                    String token = session != null && session.getLastActiveToken() != null ? session.getLastActiveToken().getJwt() : null;

//                    We are compelled to use the Clerk.INSTANCE as it's the most relievable source of up-to-date jwt token preventing stale tokens
                    var sessions = Clerk.INSTANCE.getClient().getSessions();
                    Session lastSession = !sessions.isEmpty() ? sessions.get(sessions.size() - 1) : null;
                    String token = null;
                    if (lastSession != null && lastSession.getLastActiveToken() != null) {
                        token = lastSession.getLastActiveToken().getJwt();
                    }
                    Log.d("HTTPs", "Session: " + token);
                    Request.Builder builder = original.newBuilder();

                    if (token != null) {
                        builder.header("Authorization", "Bearer " + token);
                    }

                    return chain.proceed(builder.build());
                })
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    public UserApi provideUserApi(Retrofit retrofit) {
        return retrofit.create(UserApi.class);
    }

    @Provides
    @Singleton
    public BookApi provideBookApi(Retrofit retrofit) { return retrofit.create(BookApi.class); }

    @Provides
    @Singleton
    public TaskApi provideTaskApi(Retrofit retrofit) { return retrofit.create(TaskApi.class); }
}
