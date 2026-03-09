package handworks_cleaning_service.handworks_mobile.data.repository;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.UserWrapper;
import handworks_cleaning_service.handworks_mobile.data.remote.UserApi;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserApi userApi;
    private Employee cachedEmployee;

    @Inject
    public UserRepository(UserApi userApi){ this.userApi = userApi; }

    public void clearCache() {
        cachedEmployee = null;
    }

    public void fetchEmployee(String userId, FetchStrategy strategy, Callback<UserWrapper<Employee>> callback) {
        if (cachedEmployee != null && (strategy == FetchStrategy.CACHE_FIRST || strategy == FetchStrategy.CACHE_ONLY)) {
            UserWrapper<Employee> wrapper = new UserWrapper<>();
            wrapper.setEmployee(cachedEmployee);
            callback.onResponse(null, Response.success(wrapper));
        }

        if (strategy == FetchStrategy.CACHE_ONLY) return;

        userApi.getEmployeeById(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserWrapper<Employee>> call, @NonNull Response<UserWrapper<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cachedEmployee = response.body().unwrap();
                    callback.onResponse(call, response);
                } else {
                    callback.onFailure(call, new Throwable("Invalid response"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserWrapper<Employee>> call, @NonNull Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
