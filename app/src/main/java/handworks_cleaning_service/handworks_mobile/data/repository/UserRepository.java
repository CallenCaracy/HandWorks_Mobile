package handworks_cleaning_service.handworks_mobile.data.repository;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeInRequest;
import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeOutRequest;
import handworks_cleaning_service.handworks_mobile.data.dto.user.UpdateEmployeeRequest;
import handworks_cleaning_service.handworks_mobile.data.models.users.TimeSheet;
import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.TimeSheetWrapper;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.UserWrapper;
import handworks_cleaning_service.handworks_mobile.data.remote.api.UserApi;
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

    public void fetchEmployee(String userId, FetchStrategy strategy, EmployeeCallback callback) {
        if (cachedEmployee != null && (strategy == FetchStrategy.CACHE_FIRST || strategy == FetchStrategy.CACHE_ONLY)) {
            UserWrapper<Employee> wrapper = new UserWrapper<>();
            wrapper.setEmployee(cachedEmployee);
            callback.onSuccess(cachedEmployee);
        }

        if (strategy == FetchStrategy.CACHE_ONLY) return;

        userApi.getEmployeeById(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserWrapper<Employee>> call, @NonNull Response<UserWrapper<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cachedEmployee = response.body().unwrap();
                    callback.onSuccess(cachedEmployee);
                } else {
                    callback.onError("Invalid response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserWrapper<Employee>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
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

    public void fetchTodayTimeSheet(String id, FetchStrategy strategy, TimeSheetCallback callback) {
        if (cachedTimeSheet != null && (strategy == FetchStrategy.CACHE_FIRST || strategy == FetchStrategy.CACHE_ONLY)) {
            callback.onSuccess(cachedTimeSheet);
            if (strategy == FetchStrategy.CACHE_ONLY) return;
        }

        userApi.getTodayTimeSheet(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TimeSheetWrapper> call, @NonNull Response<TimeSheetWrapper> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getTimeSheet() != null) {
                    cachedTimeSheet = response.body().getTimeSheet();

                    callback.onSuccess(cachedTimeSheet);
                } else {
                    callback.onError("Invalid response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimeSheetWrapper> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateEmployee(String empId, UpdateEmployeeRequest request, Callback<Employee> callback) {
        userApi.updateEmployee(empId, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                if (response.isSuccessful()) cachedEmployee = response.body();
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public interface EmployeeCallback {
        void onSuccess(Employee data);
        void onError(String message);
    }

    public interface TimeSheetCallback {
        void onSuccess(TimeSheet data);
        void onError(String message);
    }
}
