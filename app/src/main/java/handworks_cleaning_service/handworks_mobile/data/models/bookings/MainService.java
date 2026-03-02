package handworks_cleaning_service.handworks_mobile.data.models.bookings;

import java.io.Serializable;

public class MainService implements Serializable {
    private String id;
    private ServiceDetail details;
    private String serviceType;

    public String getId() { return id; }
    public ServiceDetail getDetails() { return details; }
    public String getServiceType() { return serviceType; }
}