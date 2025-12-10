package handworks_cleaning_service.handworks_mobile.data.models.employee;

public class Employee {
    private String id;
    private String accountId;
    private String position;
    private String status;
    private float performanceScore;
    private String hireDate;
    private int numRatings;
    private String createdAt;
    private String updatedAt;

    public Employee(String id, String accountId, String position, String status, float performanceScore, String hireDate, int numRatings, String createdAt, String updatedAt) {
        this.id = id;
        this.accountId = accountId;
        this.position = position;
        this.status = status;
        this.performanceScore = performanceScore;
        this.hireDate = hireDate;
        this.numRatings = numRatings;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPosition() {
        return position;
    }

    public String getStatus() {
        return status;
    }

    public float getPerformanceScore() {
        return performanceScore;
    }

    public String getHireDate() {
        return hireDate;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(String id) { this.id = id; }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPerformanceScore(float performanceScore) { this.performanceScore = performanceScore; }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
