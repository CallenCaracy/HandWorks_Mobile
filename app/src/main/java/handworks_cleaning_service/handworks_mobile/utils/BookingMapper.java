package handworks_cleaning_service.handworks_mobile.utils;

import java.util.ArrayList;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.Addon;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Asset;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Cleaner;
import handworks_cleaning_service.handworks_mobile.ui.models.BookingUiModel;

public class BookingMapper {
    public static BookingUiModel toUiModel(Booking booking) {
        String customerName = booking.getBase().getCustomerFirstName() +
                " " + booking.getBase().getCustomerLastName();

        String serviceType = booking.getMainService() != null ?
                EnumHelper.getReadableServiceType(null, booking.getMainService().getServiceType())
                : "Unknown";

        List<String> addonNames = new ArrayList<>();
        if (booking.getAddons() != null) {
            for (Addon a : booking.getAddons()) {
                addonNames.add(a.getServiceDetail().getServiceType());
            }
        }

        List<String> cleanerNames = new ArrayList<>();
        if (booking.getCleaners() != null) {
            for (Cleaner c : booking.getCleaners()) {
                cleanerNames.add(c.getCleanerFirstName() + " " + c.getCleanerLastName());
            }
        }

        List<String> equipmentNames = new ArrayList<>();
        if (booking.getEquipments() != null) {
            for (Asset e : booking.getEquipments()) {
                equipmentNames.add(e.getName());
            }
        }

        String address = booking.getBase().getAddress() != null ?
                booking.getBase().getAddress().getAddressHuman() : "";

        List<String> photoUrls = booking.getBase().getPhotos();

        int dirtyScale = booking.getBase().getDirtyScale();

        return new BookingUiModel(
                booking.getId(),
                customerName,
                serviceType,
                DateUtil.extractDateFromISO8601TimeStamps(booking.getBase().getStartSched()),
                DateUtil.extractTimeFromISO8601TimeStamps(booking.getBase().getEndSched()),
                booking.getTotalPrice(),
                address,
                dirtyScale,
                photoUrls,
                addonNames,
                cleanerNames,
                equipmentNames
        );
    }
}