package handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.car;

import java.io.Serializable;

public class CarCleaningSpecification implements Serializable {

    private String carType;
    private int quantity;

    public CarCleaningSpecification(String carType, int quantity) {
        this.carType = carType;
        this.quantity = quantity;
    }

    public String getCarType() { return carType; }
    public int getQuantity() { return quantity; }
}
