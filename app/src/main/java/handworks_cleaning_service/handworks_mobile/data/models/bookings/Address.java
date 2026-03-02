package handworks_cleaning_service.handworks_mobile.data.models.bookings;

import java.io.Serializable;

public class Address implements Serializable {
    private String addressHuman;
    private double addressLat;
    private double addressLng;

    public String getAddressHuman() { return addressHuman; }
    public double getAddressLat() { return addressLat; }
    public double getAddressLng() { return addressLng; }
}