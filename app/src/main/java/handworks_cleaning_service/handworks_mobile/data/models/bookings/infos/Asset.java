package handworks_cleaning_service.handworks_mobile.data.models.bookings.infos;

import java.io.Serializable;

public class Asset implements Serializable {
    private String id;
    private String name;
    private String photoUrl;
    private String type;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhotoUrl() { return photoUrl; }
    public String getType() { return type; }
}