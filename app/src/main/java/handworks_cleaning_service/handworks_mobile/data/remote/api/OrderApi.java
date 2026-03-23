package handworks_cleaning_service.handworks_mobile.data.remote.api;

import handworks_cleaning_service.handworks_mobile.data.models.orders.Order;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrderApi {
    @GET("payment/order")
    Call<Order> getOrderById(@Query("id") String orderId);
}
