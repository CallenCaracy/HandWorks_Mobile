package handworks_cleaning_service.handworks_mobile.ui.models;

import handworks_cleaning_service.handworks_mobile.R;

public enum PaymentStatus {
    PENDING_FULLPAYMENT("Pending Full Payment", R.color.gray, 0),
    PAID("Paid", R.color.green, 0);

    public final String label;
    public final int colorRes;
    public final int iconRes;

    PaymentStatus(String label, int colorRes, int iconRes) {
        this.label = label;
        this.colorRes = colorRes;
        this.iconRes = iconRes;
    }

    public static PaymentStatus fromBackend(String backendStatus) {
        if (backendStatus.equals("PAID")) {
            return PAID;
        } else {
            return PENDING_FULLPAYMENT;
        }
    }
}
