package handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.couch;

import java.io.Serializable;

public class CouchCleaningSpecification implements Serializable {

    private String couchType;
    private int depthCm;
    private int heightCm;
    private int widthCm;
    private int quantity;

    public CouchCleaningSpecification(String couchType, int depthCm,
                                      int heightCm, int widthCm, int quantity) {
        this.couchType = couchType;
        this.depthCm = depthCm;
        this.heightCm = heightCm;
        this.widthCm = widthCm;
        this.quantity = quantity;
    }
}