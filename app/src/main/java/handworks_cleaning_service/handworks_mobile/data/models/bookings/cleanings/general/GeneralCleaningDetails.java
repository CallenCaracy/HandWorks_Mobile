package handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.general;

import java.io.Serializable;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.CleaningDetails;

public class GeneralCleaningDetails extends CleaningDetails implements Serializable {

    private String homeType;
    private int hours;
    private int sqm;

    public GeneralCleaningDetails() {}

    public GeneralCleaningDetails(String homeType, int hours, int sqm) {
        this.homeType = homeType;
        this.hours = hours;
        this.sqm = sqm;
    }
}