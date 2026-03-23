package handworks_cleaning_service.handworks_mobile.ui.viewmodel;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.PAGE_LIMIT;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import handworks_cleaning_service.handworks_mobile.data.dto.book.BooksByEmployeeIdRequest;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.BookingWrapper;
import handworks_cleaning_service.handworks_mobile.data.repository.BookRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import handworks_cleaning_service.handworks_mobile.data.repository.config.PaginationState;
import handworks_cleaning_service.handworks_mobile.utils.uistate.UIState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class BookViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final MutableLiveData<UIState<List<Booking>>> bookingsState = new MutableLiveData<>();
    private final MutableLiveData<UIState<Booking>> bookingByIdState = new MutableLiveData<>();
    private int totalBookings;

    @Inject
    public BookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<UIState<List<Booking>>> getBookingsState() {
        return bookingsState;
    }
    public LiveData<UIState<Booking>> getBookingByIdState() {
        return bookingByIdState;
    }

    public void resetPagination(String employeeId, String startDate, String endDate) {
        PaginationState state = bookRepository.getPaginationState(employeeId, startDate, endDate);
        state.reset();

        bookingsState.setValue(UIState.success(new ArrayList<>()));
    }

    public void restoreCachedOrLoad(String employeeId, String startDate, String endDate) {
        PaginationState state = bookRepository.getPaginationState(employeeId, startDate, endDate);
//
//        if (!state.getAccumulated().isEmpty()) {
//            totalBookings = state.getTotalBookings();
//            bookingsState.setValue(UIState.success(new ArrayList<>(state.getAccumulated())));
//        } else {
//            resetPagination(employeeId, startDate, endDate);
//            loadNextPage(employeeId, startDate, endDate, FetchStrategy.NETWORK_ONLY);
//        }

        List<Booking> cached = bookRepository.getCachedPage(employeeId, startDate, endDate, 0);

        if (cached != null && !cached.isEmpty()) {
            state.append(cached);
            state.nextPage();
            bookingsState.setValue(UIState.success(state.getAccumulated()));
        } else {
            resetPagination(employeeId, startDate, endDate);
            loadNextPage(employeeId, startDate, endDate, FetchStrategy.NETWORK_ONLY);
        }
    }

    public void loadNextPage(String employeeId, String startDate, String endDate, FetchStrategy strategy) {

        PaginationState state = bookRepository.getPaginationState(employeeId, startDate, endDate);

        if (!state.canLoadMore()) return;
        state.setLoading(true);
        bookingsState.setValue(UIState.loading());

        List<Booking> cached = bookRepository.getCachedPage(employeeId, startDate, endDate, state.getCurrentPage());
        if (strategy == FetchStrategy.CACHE_ONLY) {
            state.setLoading(false);

            if (cached != null) {
                bookingsState.setValue(UIState.success(state.getAccumulated()));
            } else {
                bookingsState.setValue(UIState.error("No cached data"));
            }

            return;
        }

        if (strategy == FetchStrategy.CACHE_FIRST && cached != null) {
            state.setLoading(false);
            state.append(cached);
            state.nextPage();
            bookingsState.setValue(UIState.success(state.getAccumulated()));
            return;
        }

        BooksByEmployeeIdRequest request = new BooksByEmployeeIdRequest(
                employeeId,
                startDate,
                endDate,
                state.getCurrentPage(),
                PAGE_LIMIT
        );

        bookRepository.fetchBookingsByEmployeeId(request, strategy, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BookingWrapper> call, @NonNull Response<BookingWrapper> response) {
                state.setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> newBookings = response.body().getData().getBookings();

                    if (newBookings == null || newBookings.isEmpty()) {
                        state.setIsLastPage(true);
                        bookingsState.setValue(UIState.success(state.getAccumulated()));
                        return;
                    }

                    if (newBookings.size() < PAGE_LIMIT) {
                        state.setIsLastPage(true);
                    }

                    totalBookings = response.body().getData().getTotalBookings();
                    bookRepository.cachePage(employeeId, startDate, endDate, state.getCurrentPage(), newBookings);
                    state.updateTotal(response.body().getData().getTotalBookings());
                    bookingsState.setValue(UIState.success(state.getAccumulated()));
                    state.nextPage();
                } else {
                    bookingsState.postValue(UIState.error("Empty or unsuccessful response"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingWrapper> call, @NonNull Throwable t) {
                state.setLoading(false);
                bookingsState.postValue(UIState.error(t.getMessage()));
                Log.e("HTTPs", "NETWORK FAILURE", t);
            }
        });
    }

    public void loadBookingById(String bookingId, FetchStrategy strategy) {
        bookingByIdState.postValue(UIState.loading());

        bookRepository.fetchBookingById(bookingId, strategy, new BookRepository.BookingCallback() {
            @Override
            public void onSuccess(Booking data) {
                if (data != null) {
                    bookingByIdState.postValue(UIState.success(data));
                } else {
                    bookingByIdState.postValue(UIState.error("Failed to fetch booking with id"));
                }
            }

            @Override
            public void onError(String message) {
                bookingByIdState.postValue(UIState.error(message));
            }
        });
    }

    public int getTotalBookings() {
        return totalBookings;
    }
}
