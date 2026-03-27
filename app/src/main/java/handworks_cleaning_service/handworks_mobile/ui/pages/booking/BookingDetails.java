package handworks_cleaning_service.handworks_mobile.ui.pages.booking;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.models.orders.Order;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityBookingDetailsBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.AddonAdapter;
import handworks_cleaning_service.handworks_mobile.ui.adapters.AssetAdapter;
import handworks_cleaning_service.handworks_mobile.ui.adapters.CleanerAdapter;
import handworks_cleaning_service.handworks_mobile.ui.models.BookingStatus;
import handworks_cleaning_service.handworks_mobile.ui.models.PaymentStatus;
import handworks_cleaning_service.handworks_mobile.ui.pages.index.FullscreenImageView;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.BookViewModel;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.OrderViewModel;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;
import handworks_cleaning_service.handworks_mobile.utils.MapServiceType;
import handworks_cleaning_service.handworks_mobile.utils.StringUtil;

@AndroidEntryPoint
public class BookingDetails extends AppCompatActivity {
    private ActivityBookingDetailsBinding binding;
    private BookViewModel bookViewModel;
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

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        OrderViewModel orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        binding = ActivityBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpRecyclerAdapters();

        binding.bookingDetailsHeader.titlePageTxt.setText(getString(R.string.booking_details));
        binding.bookingDetailsHeader.btnExit.setOnClickListener(v -> finish());

        binding.toggleDetails.setText(R.string.view_details);
        binding.toggleDetails.setOnClickListener(v -> {
            TransitionSet set = new TransitionSet();
            set.setDuration(200);
            set.setInterpolator(new FastOutSlowInInterpolator());

            TransitionManager.beginDelayedTransition(
                    binding.detailsContainer,
                    set
            );

            if (binding.detailsContainer.getVisibility() == View.GONE) {
                binding.detailsContainer.setVisibility(View.VISIBLE);
                binding.toggleDetails.setText(R.string.hide_details);
            } else {
                binding.detailsContainer.setVisibility(View.GONE);
                binding.toggleDetails.setText(R.string.view_details);
            }
        });

        Booking bookingFromIntent = (Booking) getIntent().getSerializableExtra("booking");
        if (bookingFromIntent != null) {
            bindBooking(bookingFromIntent);
            String orderId = bookingFromIntent.getBase().getOrderId();
            orderViewModel.loadOrderById(orderId, FetchStrategy.CACHE_FIRST);
        }

        // Passed from calendar
        String bookingId = getIntent().getStringExtra("BOOKING_ID");
        if (bookingId != null && !bookingId.isEmpty()) {
            bookViewModel.loadBookingById(bookingId, FetchStrategy.CACHE_FIRST);
        }

        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (bookingFromIntent != null) {
                bookViewModel.loadBookingById(bookingFromIntent.getId(), FetchStrategy.NETWORK_ONLY);
            }
        });

        bookViewModel.getBookingByIdState().observe(this, state -> {
            binding.swipeRefresh.setRefreshing(false);

            switch (state.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    bindBooking(state.getData());
                    String orderId = state.getData().getBase().getOrderId();
                    orderViewModel.loadOrderById(orderId, FetchStrategy.NETWORK_ONLY);
                    break;
                case ERROR:
                    Toast.makeText(this, state.getMessage(), LENGTH_SHORT).show();
                    break;
            }
        });

        orderViewModel.getOrderByIdState().observe(this, state -> {
            switch (state.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    Order order = state.getData();
                    binding.orderNumber.setText(
                            getString(R.string.order_number, order.getOrder_number())
                    );

                    PaymentStatus status = PaymentStatus.fromBackend(order.getPayment_status());
                    binding.paymentStatus.setText(status.label);
                    binding.paymentStatus.setBackgroundTintList(
                            ContextCompat.getColorStateList(this, status.colorRes)
                    );

                    binding.paymentMethod.setText(
                            getString(R.string.payment_method, StringUtil.capitalizeFirstLetter(order.getPayment_method()))
                    );
                    binding.addonValue.setText(
                            getString(R.string.price_format, order.getAddon_total())
                    );
                    binding.downpaymentValue.setText(String.valueOf(order.getDownpayment_required()));
                    binding.subtotalValue.setText(String.valueOf(order.getSubtotal()));
                    binding.totalValue.setText(String.valueOf(order.getTotal_amount()));
                    binding.remainingValue.setText(String.valueOf(order.getRemaining_balance()));
                    break;

                case ERROR:
                    Toast.makeText(this, state.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void bindBooking(Booking booking) {

        if (booking != null){
            String customerFullName = booking.getBase().getCustomerFirstName() + ' ' + booking.getBase().getCustomerLastName();
            binding.customerNameText.setText(customerFullName);

            BookingStatus status = BookingStatus.fromBackend(booking.getBase().getStatus());
            binding.progressStatus.setText(status.label);
            binding.progressStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, status.colorRes)
            );

            binding.dirtyScaleText.setText(
                    getString(R.string.dirty_scale, booking.getBase().getDirtyScale())
            );

            String isoDate = booking.getBase().getStartSched();
            String startTime = DateUtil.extractTimeFromISO8601TimeStamps(isoDate);
            String endTime = DateUtil.extractTimeFromISO8601TimeStamps(booking.getBase().getEndSched());
            int extraHours = booking.getBase().getExtraHours();

            if (extraHours > 0) {
                endTime = DateUtil.addExtraHours(endTime, extraHours);

                String extraText = String.format(Locale.getDefault(), " (Extra %d hours)", extraHours);

                binding.startAndEndTimeText.setText(
                        getString(R.string.time_range, startTime, endTime, extraText)
                );

                binding.extraHourCostText.setText(
                        getString(R.string.price_format, booking.getBase().getExtraHourCost())
                );
            } else {
                binding.startAndEndTimeText.setText(
                        getString(R.string.time_range, startTime, endTime, "")
                );
                binding.extraHourCostContainer.setVisibility(GONE);
            }

            binding.customerPhoneText.setText(booking.getBase().getCustomerPhoneNo().isEmpty() ? "Phone Number: N/A" : getString(R.string.customer_number, booking.getBase().getCustomerPhoneNo()));

            binding.workDateText.setText(
                    getString(R.string.scheduled_at, DateUtil.extractDateFromISO8601TimeStamps(isoDate))
            );
            binding.addressText.setText(
                    getString(R.string.cleaning_site, booking.getBase().getAddress().getAddressHuman())
            );
            binding.totalPriceText.setText(
                    getString(R.string.price_format, booking.getTotalPrice())
            );

            setupImages(booking.getBase().getPhotos());

            binding.mainServiceDetailsText.setText(MapServiceType.getReadableServiceDetails(booking.getMainService().getDetails()));

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

    private void setUpRecyclerAdapters() {
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
        binding.flexLayout.removeAllViews();
        binding.noSiteImages.setVisibility(View.GONE);

        if (photoUrls == null || photoUrls.isEmpty()) {
            binding.noSiteImages.setVisibility(View.VISIBLE);
            return;
        }

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