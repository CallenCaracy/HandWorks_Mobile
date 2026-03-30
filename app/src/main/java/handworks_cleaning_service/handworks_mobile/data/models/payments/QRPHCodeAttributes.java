package handworks_cleaning_service.handworks_mobile.data.models.payments;

public class QRPHCodeAttributes {
    private String name;
    private String kind;
    private String mobile_number;
    private String qr_image;
    private String status;
    private Boolean livemode;
    private String notes;
    private String reference_id;
    private int created_at;

    public QRPHCodeAttributes(String name, String kind, String mobile_number, String qr_image, String status, Boolean livemode, String notes, String reference_id, int created_at) {
        this.name = name;
        this.kind = kind;
        this.mobile_number = mobile_number;
        this.qr_image = qr_image;
        this.status = status;
        this.livemode = livemode;
        this.notes = notes;
        this.reference_id = reference_id;
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getQr_image() {
        return qr_image;
    }

    public void setQr_image(String qr_image) {
        this.qr_image = qr_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }
}
