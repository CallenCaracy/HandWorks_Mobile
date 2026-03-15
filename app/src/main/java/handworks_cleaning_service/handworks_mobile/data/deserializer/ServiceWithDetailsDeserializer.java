package handworks_cleaning_service.handworks_mobile.data.deserializer;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.CAR;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.COUCH;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.GENERAL_CLEANING;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.MATTRESS;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.POST;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.CleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.car.CarCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.couch.CouchCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.general.GeneralCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.mattress.MattressCleaningDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.cleanings.postconstruction.PostConstructionDetails;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.services.MainService;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.services.ServiceDetail;

public class ServiceWithDetailsDeserializer<T> implements JsonDeserializer<T> {
    private final Class<T> clazz;

    public ServiceWithDetailsDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        String id = obj.get("id").getAsString();
        String serviceType = obj.get("serviceType").getAsString();

        CleaningDetails details = switch (serviceType) {
            case GENERAL_CLEANING -> context.deserialize(
                    obj.get("details"),
                    GeneralCleaningDetails.class
            );
            case COUCH -> context.deserialize(
                    obj.get("details"),
                    CouchCleaningDetails.class
            );
            case CAR -> context.deserialize(
                    obj.get("details"),
                    CarCleaningDetails.class
            );
            case MATTRESS -> context.deserialize(
                    obj.get("details"),
                    MattressCleaningDetails.class
            );
            case POST -> context.deserialize(
                    obj.get("details"),
                    PostConstructionDetails.class
            );
            default -> throw new JsonParseException(
                    "Unknown serviceType: " + serviceType);
        };

        try {
            T service = clazz.getDeclaredConstructor().newInstance();

            if (service instanceof MainService main) {
                main.setId(id);
                main.setServiceType(serviceType);
                main.setDetails(details);
            } else if (service instanceof ServiceDetail detail) {
                detail.setId(id);
                detail.setServiceType(serviceType);
                detail.setDetails(details);
            }

            return service;

        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}