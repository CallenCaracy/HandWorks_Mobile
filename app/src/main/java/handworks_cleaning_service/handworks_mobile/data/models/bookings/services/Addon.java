package handworks_cleaning_service.handworks_mobile.data.models.bookings.services;

import java.io.Serializable;

public class Addon implements Serializable {
    private String id;
    private double price;
    private ServiceDetail serviceDetail;

    public String getId() { return id; }
    public double getPrice() { return price; }
    public ServiceDetail getServiceDetail() { return serviceDetail; }
}