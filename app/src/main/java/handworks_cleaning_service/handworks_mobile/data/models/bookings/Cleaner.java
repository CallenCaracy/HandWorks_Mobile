package handworks_cleaning_service.handworks_mobile.data.models.bookings;

import java.io.Serializable;

public class Cleaner implements Serializable {
    private String id;
    private String cleanerFirstName;
    private String cleanerLastName;
    private String pfpUrl;

    public String getId() { return id; }
    public String getCleanerFirstName() { return cleanerFirstName; }
    public String getCleanerLastName() { return cleanerLastName; }
    public String getPfpUrl() { return pfpUrl; }
}