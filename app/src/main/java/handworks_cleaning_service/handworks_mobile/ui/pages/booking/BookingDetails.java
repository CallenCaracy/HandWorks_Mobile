package handworks_cleaning_service.handworks_mobile.ui.pages.booking;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.services.Addon;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityBookingDetailsBinding;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;
import handworks_cleaning_service.handworks_mobile.utils.EnumHelper;

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

        binding.btnExitBookingDetails.setOnClickListener(v -> finish());

        Booking booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking != null){
            String customerFullName = booking.getBase().getCustomerFirstName() + ' ' + booking.getBase().getCustomerLastName();
            binding.customerNameText.setText(customerFullName);

            binding.dirtyScaleText.setText("Dirty Scale: " + String.valueOf(booking.getBase().getDirtyScale()) + "/5");

            String isoDate = booking.getBase().getStartSched();
            String startTime = DateUtil.extractTimeFromISO8601TimeStamps(isoDate);
            String endTime = DateUtil.extractTimeFromISO8601TimeStamps(
                    booking.getBase().getEndSched()
            );

            binding.workDateText.setText("Scheduled at:" + DateUtil.extractDateFromISO8601TimeStamps(isoDate));
            binding.startAndEndTimeText.setText(startTime + " - " + endTime);

            binding.addressText.setText("Cleaning Site: " + booking.getBase().getAddress().getAddressHuman());

            binding.totalPriceText.setText("Total Price: " + String.valueOf(booking.getTotalPrice()));

            List<String> photoUrls = booking.getBase().getPhotos();
            if (photoUrls.isEmpty()) {
                binding.noSiteImages.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < photoUrls.size(); i++) {
                    var imageView = new ImageView(this);
                    var params = new FlexboxLayout.LayoutParams(200, 200);
                    params.setMargins(8, 8, 8, 8);
                    imageView.setLayoutParams(params);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(this).load(photoUrls.get(i)).into(imageView);
                    binding.flexLayout.addView(imageView);
                }
            }

            binding.mainServiceDetailsText.setText(booking.getMainService().getDetails().toString());
            binding.mainServiceTypeText.setText(EnumHelper.getReadableServiceType(this, booking.getMainService().getServiceType()));


            binding.equipmentAllocatedName.setText(booking.getEquipments().get(0).getName());
            binding.equipmentAllocatedName.setText(booking.getResources().get(0).getName());
            if (booking.getAddons() != null && !booking.getAddons().isEmpty()) {
                List<String> addonNames = new ArrayList<>();
                for (Addon addon : booking.getAddons()) {
                    if (addon.getServiceDetail() != null) {
                        addonNames.add(addon.getServiceDetail().getServiceType());
                    }
                }
                binding.addonServiceDetails.setText("Addons: " + String.join(", ", addonNames));
            } else {
                binding.addonServiceDetails.setText("Addons: None");
            }
            binding.cleanersAssignedFullName.setText(booking.getCleaners().get(0).getCleanerFirstName().toString());
        }
    }
}