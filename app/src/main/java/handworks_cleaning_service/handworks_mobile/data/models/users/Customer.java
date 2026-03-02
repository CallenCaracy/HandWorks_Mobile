package handworks_cleaning_service.handworks_mobile.data.models.users;

import handworks_cleaning_service.handworks_mobile.data.models.Account;

public class Customer {
    private String id;
    private Account account;

    public Customer(String id, Account account) {
        this.id = id;
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
