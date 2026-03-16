package handworks_cleaning_service.handworks_mobile.data.dto.user;

public class UpdateEmployeeRequest {

    private String email;
    private String employee_id;
    private String first_name;
    private String id;
    private String last_name;

    public UpdateEmployeeRequest(String email, String employee_id, String first_name, String id, String last_name) {
        this.email = email;
        this.employee_id = employee_id;
        this.first_name = first_name;
        this.id = id;
        this.last_name = last_name;
    }

    public String getEmail() { return email; }
    public String getEmployee_id() { return employee_id; }
    public String getFirst_name() { return first_name; }
    public String getId() { return id; }
    public String getLast_name() { return last_name; }
}