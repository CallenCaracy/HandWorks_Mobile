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
import handworks_cleaning_service.handworks_mobile.data.repository.config.PaginationState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class BookViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final MutableLiveData<List<Booking>> bookingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLive = new MutableLiveData<>(false);
    private boolean isLoading = false;
    private int totalBookings;

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLive;
    }

    @Inject
    public BookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<List<Booking>> getBookings() {
        return bookingsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void resetPagination(String employeeId, String startDate, String endDate) {
        PaginationState state = bookRepository.getPaginationState(employeeId, startDate, endDate);
        state.resetPage();
        state.setIsLastPage(false);

        bookingsLiveData.setValue(new ArrayList<>());
    }

    public void restoreCachedOrLoad(String employeeId, String startDate, String endDate) {
        PaginationState state = bookRepository.getPaginationState(employeeId, startDate, endDate);

        if (!state.getAccumulated().isEmpty()) {
            totalBookings = state.getTotalBookings();
            bookingsLiveData.setValue(new ArrayList<>(state.getAccumulated()));
        } else {
            resetPagination(employeeId, startDate, endDate);
            loadNextPage(employeeId, startDate, endDate);
        }
    }

    public void loadNextPage(String employeeId, String startDate, String endDate) {

        PaginationState state = bookRepository.getPaginationState(employeeId, startDate, endDate);

        if (isLoading || state.isLastPage()) return;

        List<Booking> cached = bookRepository.getCachedPage(employeeId, startDate, endDate, state.getCurrentPage());
        if (cached != null) {
            bookingsLiveData.setValue(new ArrayList<>(cached));
            state.incrementPage();
            return;
        }

        isLoading = true;
        isLoadingLive.setValue(true);

        BooksByEmployeeIdRequest request = new BooksByEmployeeIdRequest(
                employeeId,
                startDate,
                endDate,
                state.getCurrentPage(),
                PAGE_LIMIT
        );

        bookRepository.fetchBookingsByEmployeeId(request, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BookingWrapper> call, @NonNull Response<BookingWrapper> response) {
                isLoading = false;
                isLoadingLive.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> newBookings = response.body().getData().getBookings();

                    if (newBookings == null || newBookings.isEmpty()) {
                        state.setIsLastPage(true);
                        return;
                    }

                    if (newBookings.size() < PAGE_LIMIT) {
                        state.setIsLastPage(true);
                    }

                    bookRepository.cachePage(employeeId, startDate, endDate, state.getCurrentPage(), newBookings);
                    state.setTotalBookings(response.body().getData().getTotalBookings());
                    state.appendToAccumulated(newBookings);
                    bookingsLiveData.setValue(new ArrayList<>(state.getAccumulated()));

                    totalBookings = response.body().getData().getTotalBookings();
                    state.incrementPage();
                } else {
                    errorLiveData.postValue("Empty or unsuccessful response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingWrapper> call, @NonNull Throwable t) {
                isLoading = false;
                isLoadingLive.setValue(false);
                errorLiveData.postValue(t.getMessage());
                Log.e("HTTPs", "NETWORK FAILURE", t);
            }
        });
    }

    public int getTotalBookings() {
        return totalBookings;
    }
}
