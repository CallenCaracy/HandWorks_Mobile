package handworks_cleaning_service.handworks_mobile.data.repository;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.models.orders.Order;
import handworks_cleaning_service.handworks_mobile.data.remote.api.OrderApi;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private final OrderApi orderApi;
    private final Map<String, Order> cachedOrders = new HashMap<>();

    @Inject
    public OrderRepository(OrderApi orderApi){
        this.orderApi = orderApi;
    }

    public void clearCache() {
        cachedOrders.clear();
    }

    public void getOrderById(String orderId, FetchStrategy strategy, OrderCallback callback) {
        if (strategy == FetchStrategy.CACHE_ONLY || strategy == FetchStrategy.CACHE_FIRST) {
            Order cached = cachedOrders.get(orderId);

            if (cached != null) {
                callback.onSuccess(cached);

                if (strategy == FetchStrategy.CACHE_ONLY) return;
            }
        }

        orderApi.getOrderById(orderId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Order order = response.body();
                    cachedOrders.put(orderId, order);

                    callback.onSuccess(order);
                } else {
                    callback.onError("Response unsuccessful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface OrderCallback {
        void onSuccess(Order data);
        void onError(String message);
    }
}
