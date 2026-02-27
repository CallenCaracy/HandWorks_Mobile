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
import handworks_cleaning_service.handworks_mobile.data.dto.book.BooksByEmployeeIdResponse;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.BookingWrapper;
import handworks_cleaning_service.handworks_mobile.data.repository.BookRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class BookViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final MutableLiveData<List<Booking>> bookingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLive = new MutableLiveData<>(false);
    private final List<Booking> accumulatedList = new ArrayList<>();
    private int currentPage = 0;
    private boolean isLastPage = false;
    private boolean isLoading = false;

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

    public void resetPagination() {
        currentPage = 0;
        isLastPage = false;
        isLoading = false;
        accumulatedList.clear();
        bookingsLiveData.setValue(accumulatedList);
    }

    public void loadNextPage(String employeeId, String startDate, String endDate) {

        if (isLoading || isLastPage) return;

        isLoading = true;
        isLoadingLive.setValue(true);

        BooksByEmployeeIdResponse request = new BooksByEmployeeIdResponse(
                employeeId,
                startDate,
                endDate,
                currentPage,
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
                        isLastPage = true;
                        return;
                    }

                    accumulatedList.addAll(newBookings);
                    bookingsLiveData.postValue(new ArrayList<>(accumulatedList));

                    currentPage++;

                } else {
                    errorLiveData.postValue("Empty or unsuccessful response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingWrapper> call, @NonNull Throwable t) {
                isLoading = false;
                isLoadingLive.setValue(false);
                errorLiveData.postValue(t.getMessage());
                Log.e("HTTPs", "NETWORK FAILURE", t); // DEBUGGING
            }
        });
    }
}
