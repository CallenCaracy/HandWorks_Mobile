package handworks_cleaning_service.handworks_mobile.utils;

import handworks_cleaning_service.handworks_mobile.data.repository.AuthRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.BookRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SessionManager {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Inject
    public SessionManager(
            AuthRepository authRepository,
            UserRepository userRepository,
            BookRepository bookRepository
    ) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public void clearSession() {
        authRepository.clearCache();
        userRepository.clearCache();
        bookRepository.clearCache();
    }
}