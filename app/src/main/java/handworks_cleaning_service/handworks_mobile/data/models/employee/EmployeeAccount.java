package handworks_cleaning_service.handworks_mobile.data.models.employee;

import handworks_cleaning_service.handworks_mobile.data.models.AccountUser;

public class EmployeeAccount {
    private AccountUser accountUser;
    private Employee employee;

    public EmployeeAccount(AccountUser accountUser, Employee employee) {
        this.accountUser = accountUser;
        this.employee = employee;
    }

    public AccountUser getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(AccountUser accountUser) {
        this.accountUser = accountUser;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
