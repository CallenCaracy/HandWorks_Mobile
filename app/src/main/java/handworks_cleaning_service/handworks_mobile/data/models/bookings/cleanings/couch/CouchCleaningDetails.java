package handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.couch;

import java.io.Serializable;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.CleaningDetails;

public class CouchCleaningDetails extends CleaningDetails implements Serializable {

    private int bedPillows;
    private List<CouchCleaningSpecification> cleaningSpecs;

    public CouchCleaningDetails() {}

    public CouchCleaningDetails(int bedPillows, List<CouchCleaningSpecification> cleaningSpecs) {
        this.bedPillows = bedPillows;
        this.cleaningSpecs = cleaningSpecs;
    }

    public int getBedPillows() { return bedPillows; }
    public List<CouchCleaningSpecification> getCleaningSpecs() { return cleaningSpecs; }
}