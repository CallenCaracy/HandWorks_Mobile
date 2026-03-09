package handworks_cleaning_service.handworks_mobile.ui.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.UserWrapper;
import handworks_cleaning_service.handworks_mobile.data.repository.UserRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Employee> employeeLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    @Inject
    public UserViewModel(UserRepository userRepository) { this.userRepository = userRepository; }

    public LiveData<Employee> getEmployee() {
        return employeeLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void loadEmployee(String id) {
        Employee cached = userRepository.getCachedEmployee();
        if (cached != null) {
            employeeLiveData.setValue(cached);
            return;
        }

        userRepository.fetchEmployee(id, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserWrapper<Employee>> call, @NonNull Response<UserWrapper<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Employee employee = response.body().unwrap();
                    employeeLiveData.postValue(employee);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserWrapper<Employee>> call, @NonNull Throwable t) {
                errorLiveData.postValue(t.getMessage());
                Log.e("HTTPs", "NETWORK FAILURE", t); // DEBUGGING
            }
        });
    }
}