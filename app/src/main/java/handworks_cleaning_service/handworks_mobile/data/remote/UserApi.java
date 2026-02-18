package handworks_cleaning_service.handworks_mobile.data.remote;

import handworks_cleaning_service.handworks_mobile.data.models.employee.Employee;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {
    @GET("account/employee/{id}")
    Call<Employee> getEmployeeById(@Path("id") String userId);
}
