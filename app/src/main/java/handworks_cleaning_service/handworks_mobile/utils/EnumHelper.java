package handworks_cleaning_service.handworks_mobile.utils;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.CAR;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.COUCH;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.GENERAL_CLEANING;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.MATTRESS;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.POST;

import android.content.Context;

import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.CleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.car.CarCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.car.CarCleaningSpecification;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.couch.CouchCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.couch.CouchCleaningSpecification;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.general.GeneralCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.mattress.MattressCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.mattress.MattressCleaningSpecification;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.postconstruction.PostConstructionDetails;

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

    public static String getReadableServiceDetails(CleaningDetails details) {
        if (details == null) return "";

        if (details instanceof GeneralCleaningDetails general) {
            return "General: " +
                    general.getHomeType() +
                    "\n\tSize: " + general.getSqm() + " sqm" +
                    ",\n\tHours: " + general.getHours();
        } else if (details instanceof CarCleaningDetails car) {
            return "Car:" +
                    "\n\tChild Seats: " + car.getChildSeats() +
                    buildCarSpecsSummary(car.getCleaningSpecs());
        } else if (details instanceof CouchCleaningDetails couch) {
            return "Couch:" +
                    "\n\tBed Pillows: " + couch.getBedPillows() +
                    buildCouchSpecsSummary(couch.getCleaningSpecs());
        } else if (details instanceof MattressCleaningDetails mattress) {
            return "Mattress: \n" +
                    buildMattressSpecsSummary(mattress.getCleaningSpecs());
        } else if (details instanceof PostConstructionDetails post) {
            return "Post Construction: \n\t" +
                    post.getSqm() + " sqm";
        }
        return "";
    }

    private static String buildCarSpecsSummary(List<? extends CarCleaningSpecification> specs) {
        if (specs == null || specs.isEmpty()) return "\nNo specs";

        StringBuilder sb = new StringBuilder();
        for (CarCleaningSpecification spec : specs) {
            sb.append("\n\tType: ").append(spec.getCarType())
                    .append(",\n\tQuantity: ").append(spec.getQuantity())
                    .append("\n\n");
        }
        return sb.toString();
    }

    private static String buildCouchSpecsSummary(List<? extends CouchCleaningSpecification> specs) {
        if (specs == null || specs.isEmpty()) return "\nNo specs";

        StringBuilder sb = new StringBuilder();
        for (CouchCleaningSpecification spec : specs) {
            sb.append("\tType: ").append(spec.getCouchType())
                    .append(",\n\tQuantity: ").append(spec.getQuantity())
                    .append(",\n\tWidth: ").append(spec.getWidthCm())
                    .append(",\n\tHeight: ").append(spec.getHeightCm())
                    .append(",\n\tDepth: ").append(spec.getDepthCm())
                    .append("\n\n");
        }
        return sb.toString();
    }

    private static String buildMattressSpecsSummary(List<? extends MattressCleaningSpecification> specs) {
        if (specs == null || specs.isEmpty()) return "\nNo specs";

        StringBuilder sb = new StringBuilder();
        for (MattressCleaningSpecification spec : specs) {
            sb.append("\tType: ").append(spec.getBedType())
                    .append(",\n\tQuantity: ").append(spec.getQuantity())
                    .append(",\n\tWidth: ").append(spec.getWidthCm())
                    .append(",\n\tHeight: ").append(spec.getHeightCm())
                    .append(",\n\tDepth: ").append(spec.getDepthCm())
                    .append("\n\n");
        }
        return sb.toString();
    }
}
