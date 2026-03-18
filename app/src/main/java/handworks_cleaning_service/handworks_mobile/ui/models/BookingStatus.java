package handworks_cleaning_service.handworks_mobile.ui.models;

import handworks_cleaning_service.handworks_mobile.R;

public enum BookingStatus {
    NOT_STARTED("Not Started", R.color.gray, 0),
    ONGOING("Ongoing", R.color.orange, 0),
    COMPLETED("Completed", R.color.green, 0);

    public final String label;
    public final int colorRes;
    public final int iconRes;

    BookingStatus(String label, int colorRes, int iconRes) {
        this.label = label;
        this.colorRes = colorRes;
        this.iconRes = iconRes;
    }

    public static BookingStatus fromBackend(String backendStatus) {
        return switch (backendStatus) {
            case "ONGOING" -> ONGOING;
            case "COMPLETED" -> COMPLETED;
            default -> NOT_STARTED;
        };
    }
}