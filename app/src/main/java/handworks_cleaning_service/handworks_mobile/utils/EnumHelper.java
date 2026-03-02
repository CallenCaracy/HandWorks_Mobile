package handworks_cleaning_service.handworks_mobile.utils;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.CAR;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.COUCH;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.GENERAL_CLEANING;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.MATTRESS;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.POST;

import android.content.Context;

import handworks_cleaning_service.handworks_mobile.R;

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
}
