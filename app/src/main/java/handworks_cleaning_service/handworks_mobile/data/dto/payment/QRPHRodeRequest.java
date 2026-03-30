package handworks_cleaning_service.handworks_mobile.data.dto.payment;

public class QRPHRodeRequest {
    private final String kind;
    private final String mobile_number;
    private final String notes;

    public QRPHRodeRequest(String kind, String mobile_number, String notes) {
        this.kind = kind;
        this.mobile_number = mobile_number;
        this.notes = notes;
    }

    public String getKind() {
        return kind;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getNotes() {
        return notes;
    }
}
