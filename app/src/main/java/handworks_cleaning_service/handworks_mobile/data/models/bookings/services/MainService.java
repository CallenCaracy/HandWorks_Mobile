package handworks_cleaning_service.handworks_mobile.data.models.bookings.services;

import java.io.Serializable;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.CleaningDetails;

public class MainService implements Serializable {
    private String id;
    private CleaningDetails details;
    private String serviceType;

    public String getId() { return id; }
    public CleaningDetails getDetails() { return details; }
    public String getServiceType() { return serviceType; }

    public void setId(String id) {
        this.id = id;
    }

    public void setDetails(CleaningDetails details) {
        this.details = details;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}