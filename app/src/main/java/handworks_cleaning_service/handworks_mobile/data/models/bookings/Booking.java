package handworks_cleaning_service.handworks_mobile.data.models.bookings;

import java.io.Serializable;
import java.util.List;

public class Booking implements Serializable {
    private String id;
    private Base base;
    private MainService mainService;
    private List<Addon> addons;
    private List<Cleaner> cleaners;
    private List<Asset> equipments;
    private List<Asset> resources;
    private double totalPrice;

    public String getId() { return id; }
    public Base getBase() { return base; }
    public MainService getMainService() { return mainService; }
    public List<Addon> getAddons() { return addons; }
    public List<Cleaner> getCleaners() { return cleaners; }
    public List<Asset> getEquipments() { return equipments; }
    public List<Asset> getResources() { return resources; }
    public double getTotalPrice() { return totalPrice; }

}
