package handworks_cleaning_service.handworks_mobile.data.remote;

import handworks_cleaning_service.handworks_mobile.data.dto.LoginRequest;
import handworks_cleaning_service.handworks_mobile.data.models.employee.EmployeeAccount;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @GET("users/{id}")
    Call<EmployeeAccount> getUserById(@Path("id") String userId);
}
