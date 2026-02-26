package handworks_cleaning_service.handworks_mobile.data.dto.book;

public class BooksByEmployeeIdRequest {
    public String employeeId;
    public String startDate; // YYYY-MM-DD
    public String endDate; // YYYY-MM-DD
    public int pageNumber;
    public int limit;
}