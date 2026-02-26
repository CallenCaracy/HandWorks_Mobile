package handworks_cleaning_service.handworks_mobile.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.models.wrappers.BookingWrapper;
import handworks_cleaning_service.handworks_mobile.data.repository.BookRepository;

public class BookViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final MutableLiveData<BookingWrapper> bookLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    @Inject
    public BookViewModel(BookRepository bookRepository) { this.bookRepository = bookRepository; }

    public void clearCache() {
        bookRepository.clearCache();
    }
}
