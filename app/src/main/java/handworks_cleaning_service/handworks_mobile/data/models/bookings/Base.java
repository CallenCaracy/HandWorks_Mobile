package handworks_cleaning_service.handworks_mobile.data.models.bookings;

import java.util.List;

public class Base {
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

    public Address getAddress() { return address; }
    public List<String> getPhotos() { return photos; }
}
