package handworks_cleaning_service.handworks_mobile.data.repository.config;

import android.content.SharedPreferences;

import handworks_cleaning_service.handworks_mobile.data.repository.AuthRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.BookRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.OrderRepository;
import handworks_cleaning_service.handworks_mobile.data.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SessionManager {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    @Inject
    SharedPreferences prefs;

    @Inject
    public SessionManager(
            AuthRepository authRepository,
            UserRepository userRepository,
            BookRepository bookRepository,
            OrderRepository orderRepository
    ) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    public void clearSession() {
        authRepository.clearCache();
        userRepository.clearCache();
        bookRepository.clearCache();
        orderRepository.clearCache();
        prefs.edit()
                .remove("EMP_ID")
                .remove("landing_seen")
                .apply();
    }
}