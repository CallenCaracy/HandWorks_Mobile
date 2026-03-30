package handworks_cleaning_service.handworks_mobile.data.remote.api;

import handworks_cleaning_service.handworks_mobile.data.dto.payment.QRPHRodeRequest;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.QRPHCodeWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentApi {
    @POST("payment/payments/intent/qrph-static")
    Call<QRPHCodeWrapper> createQRPHStaticCode(@Body QRPHRodeRequest request);
}
