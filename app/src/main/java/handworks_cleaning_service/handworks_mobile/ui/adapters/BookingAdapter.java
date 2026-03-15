package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;

public class BookingAdapter extends RecyclerView.Adapter<BookingViewHolder> {
    private OnBookingClickListener listener;
    private List<Booking> bookings = new ArrayList<>();

    public BookingAdapter() {}

    public BookingAdapter(List<Booking> bookings, OnBookingClickListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    public void submitList(List<Booking> newList) {
        bookings.clear();
        if (newList != null) {
            bookings.addAll(newList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking);

        holder.itemView.setOnClickListener(v -> listener.onBookingClick(booking));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public interface OnBookingClickListener {
        void onBookingClick(Booking booking);
    }
}