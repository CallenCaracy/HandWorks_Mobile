package handworks_cleaning_service.handworks_mobile.data.repository;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeInRequest;
import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeOutRequest;
import handworks_cleaning_service.handworks_mobile.data.models.users.TimeSheet;
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
    private TimeSheet cachedTimeSheet;

    @Inject
    public UserRepository(UserApi userApi){ this.userApi = userApi; }

    public void clearCache() {
        cachedEmployee = null;
        cachedTimeSheet = null;
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

    public void timeIn(TimeInRequest request, Callback<TimeSheet> callback) {
        userApi.employeeTimeIn(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TimeSheet> call, @NonNull Response<TimeSheet> response) {
                if (response.isSuccessful()) cachedTimeSheet = response.body();
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<TimeSheet> call, @NonNull Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void timeOut(TimeOutRequest request, Callback<TimeSheet> callback) {
        userApi.employeeTimeOut(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TimeSheet> call, @NonNull Response<TimeSheet> response) {
                if (response.isSuccessful()) cachedTimeSheet = response.body();
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<TimeSheet> call, @NonNull Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void fetchTodayTimeSheet(String id, FetchStrategy strategy, Callback<TimeSheet> callback) {
        if (cachedTimeSheet != null && (strategy == FetchStrategy.CACHE_FIRST || strategy == FetchStrategy.CACHE_ONLY)) {
            TimeSheet timeSheet = cachedTimeSheet;
            callback.onResponse(null, Response.success(timeSheet));
        }

        if (strategy == FetchStrategy.CACHE_ONLY) return;

        userApi.getTodayTimeSheet(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TimeSheet> call, @NonNull Response<TimeSheet> response) {
                if (response.isSuccessful()) cachedTimeSheet = response.body();
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<TimeSheet> call, @NonNull Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
