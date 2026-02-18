package handworks_cleaning_service.handworks_mobile.data.repository;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.models.employee.Employee;
import handworks_cleaning_service.handworks_mobile.data.remote.UserApi;
import retrofit2.Call;

public class UserRepository {
    private final UserApi userApi;

    @Inject
    public UserRepository(UserApi userApi){
        this.userApi = userApi;
    }

    public Call<Employee> getEmployeeById(String userId) {
        return userApi.getEmployeeById(userId);
    }
}
