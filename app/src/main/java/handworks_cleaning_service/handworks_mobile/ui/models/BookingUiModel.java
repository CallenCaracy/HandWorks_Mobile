package handworks_cleaning_service.handworks_mobile.ui.models;

import java.util.List;

public class BookingUiModel {
    private String id;
    private String customerName;
    private String serviceType;
    private String startDate;
    private String endDate;
    private double totalPrice;
    private String address;
    private int dirtyScale;
    private List<String> photoUrls;
    private List<String> addonNames;
    private List<String> cleanerNames;
    private List<String> equipmentNames;

    public BookingUiModel(String id,
                          String customerName,
                          String serviceType,
                          String startDate,
                          String endDate,
                          double totalPrice,
                          String address,
                          int dirtyScale,
                          List<String> photoUrls,
                          List<String> addonNames,
                          List<String> cleanerNames,
                          List<String> equipmentNames) {
        this.id = id;
        this.customerName = customerName;
        this.serviceType = serviceType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.address = address;
        this.dirtyScale = dirtyScale;
        this.photoUrls = photoUrls;
        this.addonNames = addonNames;
        this.cleanerNames = cleanerNames;
        this.equipmentNames = equipmentNames;
    }

    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getServiceType() { return serviceType; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public double getTotalPrice() { return totalPrice; }
    public String getAddress() { return address; }
    public int getDirtyScale() { return dirtyScale; }
    public List<String> getPhotoUrls() { return photoUrls; }
    public List<String> getAddonNames() { return addonNames; }
    public List<String> getCleanerNames() { return cleanerNames; }
    public List<String> getEquipmentNames() { return equipmentNames; }
}
