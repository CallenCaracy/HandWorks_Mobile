package handworks_cleaning_service.handworks_mobile.data.dto.user;

import com.google.gson.annotations.SerializedName;

public class TimeOutRequest {

    @SerializedName("employee_id")
    private final String employeeId;

    @SerializedName("time_out")
    private final String timeOut;

    public TimeOutRequest(String employeeId, String timeOut) {
        this.employeeId = employeeId;
        this.timeOut = timeOut;
    }
}