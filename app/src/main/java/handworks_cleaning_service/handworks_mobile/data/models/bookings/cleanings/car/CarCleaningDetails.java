package handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.car;

import java.io.Serializable;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.CleaningDetails;

public class CarCleaningDetails extends CleaningDetails implements Serializable {

    private int childSeats;
    private List<CarCleaningSpecification> cleaningSpecs;

    public CarCleaningDetails() {}

    public CarCleaningDetails(int childSeats, List<CarCleaningSpecification> cleaningSpecs) {
        this.childSeats = childSeats;
        this.cleaningSpecs = cleaningSpecs;
    }

    public int getChildSeats() { return childSeats; }
    public List<CarCleaningSpecification> getCleaningSpecs() { return cleaningSpecs; }
}

