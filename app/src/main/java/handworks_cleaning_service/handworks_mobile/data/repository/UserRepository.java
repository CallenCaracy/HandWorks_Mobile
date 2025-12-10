package handworks_cleaning_service.handworks_mobile.data.repository;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.remote.UserApi;

public class UserRepository {
    private final UserApi userApi;

    @Inject
    public UserRepository(UserApi userApi){
        this.userApi = userApi;
    }

    public UserApi getUserApi() {
        return userApi;
    }
}
