package handworks_cleaning_service.handworks_mobile.ui.pages.booking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Addon;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityBookingDetailsBinding;

public class BookingDetails extends AppCompatActivity {
    private ActivityBookingDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Booking booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking != null){
            binding.customerNameText.setText(booking.getBase().getCustomerFirstName());
            binding.addressText.setText(booking.getBase().getAddress().getAddressHuman());
            binding.serviceTypeText.setText(booking.getMainService().getServiceType());
            binding.startDateText.setText(booking.getBase().getStartSched());
            binding.endDateText.setText(booking.getBase().getEndSched());
            binding.dirtyScaleText.setText(String.valueOf(booking.getBase().getDirtyScale()));
            binding.totalPriceText.setText(String.valueOf(booking.getTotalPrice()));
            if (booking.getAddons() != null && !booking.getAddons().isEmpty()) {
                List<String> addonNames = new ArrayList<>();
                for (Addon addon : booking.getAddons()) {
                    if (addon.getServiceDetail() != null) {
                        addonNames.add(addon.getServiceDetail().getServiceType());
                    }
                }
                binding.addonsText.setText("Addons: " + String.join(", ", addonNames));
            } else {
                binding.addonsText.setText("Addons: None");
            }
            binding.cleanersText.setText(booking.getCleaners().toString());
        }
    }
}