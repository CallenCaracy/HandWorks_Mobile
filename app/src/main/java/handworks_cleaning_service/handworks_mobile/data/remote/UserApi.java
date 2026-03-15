package handworks_cleaning_service.handworks_mobile.data.remote;

import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeInRequest;
import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeOutRequest;
import handworks_cleaning_service.handworks_mobile.data.models.users.TimeSheet;
import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.UserWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    @GET("account/employee")
    Call<UserWrapper<Employee>> getEmployeeById(@Query("id") String userId);

    @POST("account/employee/timesheet/timein")
    Call<TimeSheet> employeeTimeIn(@Body TimeInRequest request);

    @POST("account/employee/timesheet/timeout")
    Call<TimeSheet> employeeTimeOut(@Body TimeOutRequest request);

    @GET("account/employee/timesheet/today")
    Call<TimeSheet> getTodayTimeSheet(@Query("id") String userId);
}
