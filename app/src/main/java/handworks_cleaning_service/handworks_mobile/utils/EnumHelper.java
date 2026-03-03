package handworks_cleaning_service.handworks_mobile.utils;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.CAR;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.COUCH;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.GENERAL_CLEANING;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.MATTRESS;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.POST;

import android.content.Context;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.CleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.car.CarCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.couch.CouchCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.general.GeneralCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.mattress.MattressCleaningDetails;

public class EnumHelper {
    public static String getReadableServiceType(Context context, String type) {
        return switch (type) {
            case GENERAL_CLEANING -> context.getString(R.string.service_general_cleaning);
            case COUCH -> context.getString(R.string.service_couch);
            case MATTRESS -> context.getString(R.string.service_mattress);
            case CAR -> context.getString(R.string.service_car);
            case POST -> context.getString(R.string.service_post);
            default -> context.getString(R.string.service_unknown);
        };
    }

    public static String getReadableMainServiceDetails(CleaningDetails details) {
        if (details instanceof GeneralCleaningDetails general) {
            return "Home Type: " + general.getHomeType() +
                    ", Size: " + general.getSqm() + " sqm" +
                    ", Hours: " + general.getHours();
        } else if (details instanceof CarCleaningDetails car) {
            return "Child Seats: " + car.getChildSeats() +
                    ", Specs: " + car.getCleaningSpecs().size();
        }else if (details instanceof CouchCleaningDetails couch) {
            return "Child Seats: " + couch.getBedPillows() +
                    ", Specs: " + couch.getCleaningSpecs().size();
        }else if (details instanceof MattressCleaningDetails mattress) {
            return "Child Seats: " + mattress.getCleaningSpecs() +
                    ", Specs: " + mattress.getCleaningSpecs().size();
        }
        return "";
    }
}
