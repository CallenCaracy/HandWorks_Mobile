package handworks_cleaning_service.handworks_mobile.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import handworks_cleaning_service.handworks_mobile.data.remote.api.BookApi;
import handworks_cleaning_service.handworks_mobile.data.remote.api.OrderApi;
import handworks_cleaning_service.handworks_mobile.data.remote.api.TaskApi;
import handworks_cleaning_service.handworks_mobile.data.remote.api.UserApi;
import handworks_cleaning_service.handworks_mobile.data.repository.AuthRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.BookRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.OrderRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.TaskRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.UserRepository;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    @Singleton
    public AuthRepository provideAuthRepository() { return new AuthRepository(); }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(UserApi userApi) { return new UserRepository(userApi); }

    @Provides
    @Singleton
    public BookRepository provideBookRepository(BookApi bookApi) { return new BookRepository(bookApi); }

    @Provides
    @Singleton
    public OrderRepository provideOrderRepository(OrderApi orderApi) { return new OrderRepository(orderApi); }

    @Provides
    @Singleton
    public TaskRepository provideTaskRepository(TaskApi taskApi) { return new TaskRepository(taskApi); }
}

