package handworks_cleaning_service.handworks_mobile.ui.pages.booking;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Intent;
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
import handworks_cleaning_service.handworks_mobile.ui.pages.index.FullscreenImageView;
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

        binding.bookingDetailsHeader.titlePageTxt.setText(getString(R.string.booking_details));
        binding.bookingDetailsHeader.btnExit.setOnClickListener(v -> finish());

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

            setupImages(booking.getBase().getPhotos());

            binding.mainServiceDetailsText.setText(EnumHelper.getReadableServiceDetails(booking.getMainService().getDetails()));

            if (booking.getAddons() != null) {
                addonAdapter.setAddons(booking.getAddons());
            } else {
                binding.addonServiceTitle.setVisibility(GONE);
                binding.addonRecycler.setVisibility(GONE);
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

    private void setupImages(List<String> photoUrls) {
        if (photoUrls.isEmpty()) {
            binding.noSiteImages.setVisibility(View.VISIBLE);
        } else {
            int parentWidth = getResources().getDisplayMetrics().widthPixels;

            for (int i = 0; i < photoUrls.size(); i++) {
                int width;
                int height;

                if (i == 0) {
                    width = MATCH_PARENT;
                    height = 600;
                } else {
                    width = parentWidth / 3;
                    height = parentWidth / 3;
                }

                ImageView imageView = new ImageView(this);
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(width, height);
                params.setMargins(8, 8, 8, 8);

                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setBackgroundResource(R.drawable.rounded_image);
                imageView.setClipToOutline(true);

                Glide.with(this)
                        .load(photoUrls.get(i))
                        .error(R.drawable.error_svgrepo_com)
                        .into(imageView);

                int finalI = i;
                imageView.setOnClickListener(v -> {
                    Intent intent = new Intent(this, FullscreenImageView.class);
                    intent.putExtra("image_url", photoUrls.get(finalI));
                    startActivity(intent);
                });

                binding.flexLayout.addView(imageView);
            }
        }
    }
}