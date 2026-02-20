package handworks_cleaning_service.handworks_mobile.data.models.users;

import handworks_cleaning_service.handworks_mobile.data.models.Account;

public class Employee {
    private String id;
    private Account account;
    private String position;
    private String status;
    private double performance_score;
    private String hire_date;
    private int num_ratings;

    public Employee(String id, Account account, String position, String status, String hire_date, int num_ratings, double performance_score) {
        this.id = id;
        this.account = account;
        this.position = position;
        this.status = status;
        this.hire_date = hire_date;
        this.num_ratings = num_ratings;
        this.performance_score = performance_score;
    }

    public String getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getPosition() {
        return position;
    }

    public String getStatus() {
        return status;
    }

    public String getHire_date() {
        return hire_date;
    }

    public int getNum_ratings() {
        return num_ratings;
    }

    public double getPerformance_score() {
        return performance_score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHire_date(String hire_date) {
        this.hire_date = hire_date;
    }

    public void setNum_ratings(int num_ratings) {
        this.num_ratings = num_ratings;
    }

    public void setPerformance_score(double performance_score) {
        this.performance_score = performance_score;
    }
}
