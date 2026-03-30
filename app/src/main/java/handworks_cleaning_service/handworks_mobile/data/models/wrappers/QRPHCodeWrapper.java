package handworks_cleaning_service.handworks_mobile.data.models.wrappers;

import handworks_cleaning_service.handworks_mobile.data.models.payments.QRPHCodeAttributes;

public class QRPHCodeWrapper {
    private String id;
    private String type;
    private QRPHCodeAttributes attributes;

    public QRPHCodeWrapper(String id, String type, QRPHCodeAttributes attributes) {
        this.id = id;
        this.type = type;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public QRPHCodeAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(QRPHCodeAttributes attributes) {
        this.attributes = attributes;
    }
}
