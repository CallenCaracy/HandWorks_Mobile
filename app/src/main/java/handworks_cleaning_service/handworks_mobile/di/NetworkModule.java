package handworks_cleaning_service.handworks_mobile.di;

import com.clerk.api.session.Session;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

import handworks_cleaning_service.handworks_mobile.BuildConfig;
import handworks_cleaning_service.handworks_mobile.data.remote.TaskApi;
import handworks_cleaning_service.handworks_mobile.data.remote.UserApi;
import handworks_cleaning_service.handworks_mobile.data.repository.AuthRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(AuthRepository authRepository) {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    Session session = authRepository.getSession();
                    String token = session != null && session.getLastActiveToken() != null ? session.getLastActiveToken().getJwt() : null;

                    Request.Builder builder = original.newBuilder();

                    if (token != null) {
                        builder.header("Authorization", "Bearer " + token);
                    }

                    return chain.proceed(builder.build());
                })
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
    public TaskApi provideTaskApi(Retrofit retrofit) { return retrofit.create(TaskApi.class); }
}
