package handworks_cleaning_service.handworks_mobile.ui.pages.booking;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityBookingDetailsBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.AddonAdapter;
import handworks_cleaning_service.handworks_mobile.ui.adapters.AssetAdapter;
import handworks_cleaning_service.handworks_mobile.ui.adapters.CleanerAdapter;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;
import handworks_cleaning_service.handworks_mobile.utils.EnumHelper;

public class BookingDetails extends AppCompatActivity {
    private ActivityBookingDetailsBinding binding;
    private AddonAdapter addonAdapter;
    private CleanerAdapter cleanerAdapter;
    private AssetAdapter equipmentAdapter;
    private AssetAdapter resourceAdapter;

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

        setUpRecyclerAdapters();

        binding.btnExitBookingDetails.setOnClickListener(v -> finish());

        Booking booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking != null){
            String customerFullName = booking.getBase().getCustomerFirstName() + ' ' + booking.getBase().getCustomerLastName();
            binding.customerNameText.setText(customerFullName);

            binding.dirtyScaleText.setText(
                    getString(R.string.dirty_scale, booking.getBase().getDirtyScale())
            );

            String isoDate = booking.getBase().getStartSched();
            String startTime = DateUtil.extractTimeFromISO8601TimeStamps(isoDate);
            String endTime = DateUtil.extractTimeFromISO8601TimeStamps(
                    booking.getBase().getEndSched()
            );

            binding.workDateText.setText(
                    getString(R.string.scheduled_at, DateUtil.extractDateFromISO8601TimeStamps(isoDate))
            );
            binding.startAndEndTimeText.setText(
                    getString(R.string.time_range, startTime, endTime)
            );
            binding.addressText.setText(
                    getString(R.string.cleaning_site, booking.getBase().getAddress().getAddressHuman())
            );
            binding.totalPriceText.setText(
                    getString(R.string.total_price, booking.getTotalPrice())
            );

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

            binding.mainServiceDetailsText.setText(EnumHelper.getReadableServiceDetails(booking.getMainService().getDetails()));

            if (booking.getAddons() != null) {
                addonAdapter.setAddons(booking.getAddons());
            }

            if (booking.getCleaners() != null) {
                cleanerAdapter.setCleaners(booking.getCleaners());
            }

            if (booking.getEquipments() != null) {
                equipmentAdapter.setAsset(booking.getEquipments());
            }

            if (booking.getEquipments() != null) {
                resourceAdapter.setAsset(booking.getResources());
            }
        }
    }

    public void setUpRecyclerAdapters() {
        addonAdapter = new AddonAdapter();
        binding.addonRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.addonRecycler.setAdapter(addonAdapter);

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        cleanerAdapter = new CleanerAdapter();
        binding.cleanerRecycler.setLayoutManager(flexboxLayoutManager);
        binding.cleanerRecycler.setAdapter(cleanerAdapter);

        equipmentAdapter = new AssetAdapter();
        binding.equipmentRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.equipmentRecycler.setAdapter(equipmentAdapter);

        resourceAdapter = new AssetAdapter();
        binding.resourceRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.resourceRecycler.setAdapter(resourceAdapter);
    }
}