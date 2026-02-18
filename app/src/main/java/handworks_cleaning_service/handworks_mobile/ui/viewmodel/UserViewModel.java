package handworks_cleaning_service.handworks_mobile.ui.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import handworks_cleaning_service.handworks_mobile.data.models.employee.Employee;
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

    public void fetchUser(String clerkUserId) {
        userRepository.getEmployeeById(clerkUserId)
                .enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                        Log.d("clerkUserId", "Fetch Triggered");
                        if (response.isSuccessful() && response.body() != null) {
                            employeeLiveData.postValue(response.body());
                        } else {
                            errorLiveData.postValue("Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                        errorLiveData.postValue(t.getMessage());
                    }
                });
    }
}
