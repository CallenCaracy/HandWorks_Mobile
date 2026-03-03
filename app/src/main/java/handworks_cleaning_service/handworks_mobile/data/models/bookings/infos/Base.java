package handworks_cleaning_service.handworks_mobile.data.models.bookings.infos;

import java.io.Serializable;
import java.util.List;

public class Base implements Serializable {
    private String id;
    private String custId;
    private String customerFirstName;
    private String customerLastName;
    private int dirtyScale;
    private String paymentStatus;
    private String reviewStatus;
    private String quoteId;
    private String startSched;
    private String endSched;
    private String createdAt;
    private String updatedAt;

    private Address address;
    private List<String> photos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public int getDirtyScale() {
        return dirtyScale;
    }

    public void setDirtyScale(int dirtyScale) {
        this.dirtyScale = dirtyScale;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getStartSched() {
        return startSched;
    }

    public void setStartSched(String startSched) {
        this.startSched = startSched;
    }

    public String getEndSched() {
        return endSched;
    }

    public void setEndSched(String endSched) {
        this.endSched = endSched;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
