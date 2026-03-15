package handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.postconstruction;

import java.io.Serializable;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.CleaningDetails;

public class PostConstructionDetails extends CleaningDetails implements Serializable {

    private int sqm;

    public PostConstructionDetails() {}

    public PostConstructionDetails(int sqm) {
        this.sqm = sqm;
    }

    public int getSqm() {
        return sqm;
    }
}