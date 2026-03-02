package handworks_cleaning_service.handworks_mobile.data.models.wrappers;

public class UserWrapper<T> {
    private T employee;
    private T customer;

    public T getEmployee() { return employee; }
    public T getCustomer() { return customer; }

    public void setEmployee(T employee) {
        this.employee = employee;
    }

    public void setCustomer(T customer) {
        this.customer = customer;
    }

    public T unwrap() {
        if (employee != null) return employee;
        if (customer != null) return customer;
        return null;
    }
}