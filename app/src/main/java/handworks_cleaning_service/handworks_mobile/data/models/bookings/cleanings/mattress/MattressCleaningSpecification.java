package handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.mattress;

import java.io.Serializable;

public class MattressCleaningSpecification implements Serializable {

    private String bedType;
    private int depthCm;
    private int heightCm;
    private int widthCm;
    private int quantity;

    public MattressCleaningSpecification(String bedType,
                                         int depthCm,
                                         int heightCm,
                                         int widthCm,
                                         int quantity) {
        this.bedType = bedType;
        this.depthCm = depthCm;
        this.heightCm = heightCm;
        this.widthCm = widthCm;
        this.quantity = quantity;
    }
}