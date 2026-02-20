package handworks_cleaning_service.handworks_mobile.data.repository;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.models.employee.Employee;
import handworks_cleaning_service.handworks_mobile.data.remote.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserApi userApi;
    private Employee cachedEmployee;

    @Inject
    public UserRepository(UserApi userApi){
        this.userApi = userApi;
    }

    public Employee getCachedEmployee() {
        return cachedEmployee;
    }

    public void fetchEmployee(String userId, Callback<Employee> callback) {

        // return cached immediately if available
        if (cachedEmployee != null) {
            callback.onResponse(null, Response.success(cachedEmployee));
            return;
        }

        userApi.getEmployeeById(userId).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cachedEmployee = response.body();
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void clearCache() {
        cachedEmployee = null;
    }
}
