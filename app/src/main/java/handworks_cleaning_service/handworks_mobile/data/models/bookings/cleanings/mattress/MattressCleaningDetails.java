package handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.mattress;

import java.io.Serializable;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.CleaningDetails;

public class MattressCleaningDetails extends CleaningDetails implements Serializable {

    private List<MattressCleaningSpecification> cleaningSpecs;

    public MattressCleaningDetails() {}

    public MattressCleaningDetails(List<MattressCleaningSpecification> cleaningSpecs) {
        this.cleaningSpecs = cleaningSpecs;
    }
}