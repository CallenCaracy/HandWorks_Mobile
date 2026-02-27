package handworks_cleaning_service.handworks_mobile.data.dto.book;

public class BooksByEmployeeIdResponse {
    public String employeeId;
    public String startDate; // YYYY-MM-DD
    public String endDate; // YYYY-MM-DD
    public int pageNumber;
    public int limit;

    public BooksByEmployeeIdResponse(String employeeId, String startDate, String endDate, int pageNumber, int limit) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pageNumber = pageNumber;
        this.limit = limit;
    }
}