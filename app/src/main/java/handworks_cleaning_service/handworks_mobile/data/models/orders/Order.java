package handworks_cleaning_service.handworks_mobile.data.models.orders;

public class Order {
    private String id;
    private String customer_id;
    private String quote_id;
    private String order_number;
    private String currency;
    private String payment_method;
    private String payment_status;
    private double downpayment_required;
    private double addon_total;
    private double subtotal;
    private double total_amount;
    private double remaining_balance;
    private String created_at;
    private String updated_at;

    public Order(String id, String customer_id, String quote_id, String order_number, String currency, String payment_method, String payment_status, double downpayment_required, double addon_total, double subtotal, double total_amount, double remaining_balance, String created_at, String updated_at) {
        this.id = id;
        this.customer_id = customer_id;
        this.quote_id = quote_id;
        this.order_number = order_number;
        this.currency = currency;
        this.payment_method = payment_method;
        this.payment_status = payment_status;
        this.downpayment_required = downpayment_required;
        this.addon_total = addon_total;
        this.subtotal = subtotal;
        this.total_amount = total_amount;
        this.remaining_balance = remaining_balance;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public double getDownpayment_required() {
        return downpayment_required;
    }

    public void setDownpayment_required(double downpayment_required) {
        this.downpayment_required = downpayment_required;
    }

    public double getAddon_total() {
        return addon_total;
    }

    public void setAddon_total(double addon_total) {
        this.addon_total = addon_total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getRemaining_balance() {
        return remaining_balance;
    }

    public void setRemaining_balance(double remaining_balance) {
        this.remaining_balance = remaining_balance;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
