package handworks_cleaning_service.handworks_mobile.ui.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeInRequest;
import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeOutRequest;
import handworks_cleaning_service.handworks_mobile.data.dto.user.UpdateEmployeeRequest;
import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.data.models.users.TimeSheet;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.UserWrapper;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import handworks_cleaning_service.handworks_mobile.data.repository.UserRepository;
import handworks_cleaning_service.handworks_mobile.utils.uistate.UIState;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<UIState<Employee>> employeeState = new MutableLiveData<>();
    private final MutableLiveData<UIState<TimeSheet>> timeSheetState = new MutableLiveData<>();

    @Inject
    public UserViewModel(UserRepository userRepository) { this.userRepository = userRepository; }

    public LiveData<UIState<Employee>> getEmployee() {
        return employeeState;
    }

    public LiveData<UIState<TimeSheet>> getTimeSheetState() { return timeSheetState; }

    public void loadEmployee(String id, FetchStrategy strategy) {
        employeeState.setValue(UIState.loading());

        userRepository.fetchEmployee(id, strategy, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserWrapper<Employee>> call, @NonNull Response<UserWrapper<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    employeeState.postValue(UIState.success(response.body().unwrap()));
                } else {
                    employeeState.postValue(UIState.error("Failed to fetch employee information."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserWrapper<Employee>> call, @NonNull Throwable t) {
                employeeState.postValue(UIState.error(t.getMessage()));
            }
        });
    }

    public void timeIn(TimeInRequest request) {
        timeSheetState.setValue(UIState.loading());

        userRepository.timeIn(request, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TimeSheet> call, @NonNull Response<TimeSheet> response) {
                if (response.isSuccessful() && response.body() != null) {
                    timeSheetState.postValue(UIState.success(response.body()));
                } else {
                    String msg = "Time in failed.";
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            String errorString = errorBody.string();
                            if (errorString.contains("duplicate key value")) {
                                msg = "You have already timed in today";
                            }
                        }
                    } catch (IOException e) {
                        Log.e("TimeInError", "Failed to read error body", e);
                    }
                    timeSheetState.postValue(UIState.error(msg));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimeSheet> call, @NonNull Throwable t) {
                timeSheetState.postValue(UIState.error(t.getMessage()));
            }
        });
    }

    public void timeOut(TimeOutRequest request) {
        timeSheetState.setValue(UIState.loading());

        userRepository.timeOut(request, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TimeSheet> call, @NonNull Response<TimeSheet> response) {
                if (response.isSuccessful() && response.body() != null) {
                    timeSheetState.postValue(UIState.success(response.body()));
                } else {
                    timeSheetState.postValue(UIState.error("Time out failed."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimeSheet> call, @NonNull Throwable t) {
                timeSheetState.postValue(UIState.error(t.getMessage()));
            }
        });
    }

    public void loadTodayTimeSheet(String id, FetchStrategy strategy) {
        timeSheetState.setValue(UIState.loading());

        userRepository.fetchTodayTimeSheet(id, strategy, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TimeSheet> call, @NonNull Response<TimeSheet> response) {
                if (response.isSuccessful() && response.body() != null) {
                    timeSheetState.postValue(UIState.success(response.body()));
                } else {
                    timeSheetState.postValue(UIState.error("Failed to fetch today's time sheet."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimeSheet> call, @NonNull Throwable t) {
                timeSheetState.postValue(UIState.error(t.getMessage()));
            }
        });
    }

    public void updateEmployeeInfo(String id, UpdateEmployeeRequest request) {
        userRepository.updateEmployee(id, request, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                if (response.isSuccessful() && response.body() != null) {
                    employeeState.postValue(UIState.success(response.body()));
                } else {
                    employeeState.postValue(UIState.error("Failed to update account information."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                employeeState.postValue(UIState.error(t.getMessage()));
            }
        });
    }
}