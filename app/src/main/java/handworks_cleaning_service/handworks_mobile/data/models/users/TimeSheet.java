package handworks_cleaning_service.handworks_mobile.data.models.users;

import com.google.gson.annotations.SerializedName;

public class TimeSheet {
    @SerializedName("id")
    private final String Id;
    @SerializedName("employee_id")
    private final String employeeId;
    @SerializedName("status")
    private final String status;
    @SerializedName("time_in")
    private final String timeIn;
    @SerializedName("time_out")
    private final String timeOut;
    @SerializedName("work_date")
    private final String workDate;
    @SerializedName("created_at")
    private final String createdAt;
    @SerializedName("updated_at")
    private final String updatedAt;

    public TimeSheet(String id, String employeeId, String status, String timeIn, String timeOut, String workDate, String createdAt, String updatedAt) {
        Id = id;
        this.employeeId = employeeId;
        this.status = status;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.workDate = workDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return Id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public String getWorkDate() {
        return workDate;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
