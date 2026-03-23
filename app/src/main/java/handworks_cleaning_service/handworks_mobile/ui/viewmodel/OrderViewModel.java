package handworks_cleaning_service.handworks_mobile.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import handworks_cleaning_service.handworks_mobile.data.models.orders.Order;
import handworks_cleaning_service.handworks_mobile.data.repository.OrderRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import handworks_cleaning_service.handworks_mobile.utils.uistate.UIState;
@HiltViewModel
public class OrderViewModel extends ViewModel {
    private final OrderRepository orderRepository;
    private final MutableLiveData<UIState<Order>> orderByIdState = new MutableLiveData<>();

    @Inject
    public OrderViewModel(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public MutableLiveData<UIState<Order>> getOrderByIdState() {
        return orderByIdState;
    }

    public void loadOrderById(String orderId, FetchStrategy strategy) {
        orderByIdState.postValue(UIState.loading());

        orderRepository.getOrderById(orderId, strategy, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Order data) {
                if (data != null) {
                    orderByIdState.postValue(UIState.success(data));
                } else {
                    orderByIdState.postValue(UIState.error("Failed to fetch booking with id"));
                }
            }

            @Override
            public void onError(String message) {
                orderByIdState.postValue(UIState.error(message));
            }
        });
    }
}
