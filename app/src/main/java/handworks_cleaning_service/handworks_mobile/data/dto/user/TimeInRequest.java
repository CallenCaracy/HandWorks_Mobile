package handworks_cleaning_service.handworks_mobile.data.dto.user;

import com.google.gson.annotations.SerializedName;

public class TimeInRequest {

    @SerializedName("employee_id")
    private final String employeeId;

    @SerializedName("time_in")
    private final String timeIn;

    public TimeInRequest(String employeeId, String timeIn) {
        this.employeeId = employeeId;
        this.timeIn = timeIn;
    }
}