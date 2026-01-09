package handworks_cleaning_service.handworks_mobile.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import handworks_cleaning_service.handworks_mobile.data.remote.UserApi;
import handworks_cleaning_service.handworks_mobile.data.repository.AuthRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.UserRepository;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    @Singleton
    public AuthRepository provideAuthRepository() {
        return new AuthRepository();
    }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(UserApi userApi) { return new UserRepository(userApi); }

}

