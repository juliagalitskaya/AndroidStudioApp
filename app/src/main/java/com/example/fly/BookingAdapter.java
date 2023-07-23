package com.example.fly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends BaseAdapter {

    // Context reference
    private Context context;

    // List of Booking objects to display
    private List<Booking> bookings;

    // Listener for handling button click events on items in the adapter
    private BookingAdapterListener listener;

    // Constructor to initialize the adapter with a Context and list of bookings
    public BookingAdapter(Context context, List<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    // Get the number of items in the list (total number of bookings)
    @Override
    public int getCount() {
        return bookings.size();
    }

    // Get the Booking object at the specified position
    @Override
    public Object getItem(int position) {
        return bookings.get(position);
    }

    // Get the item ID at the specified position (use position as the ID)
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Create and populate the view for each item in the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // If the view is being created for the first time, inflate the layout and create a ViewHolder
            convertView = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
            holder = new ViewHolder();
            holder.tvPassengerName = convertView.findViewById(R.id.tvPassengerName);
            holder.tvRoute = convertView.findViewById(R.id.tvRoute);
            holder.tvTicketType = convertView.findViewById(R.id.tvTicketType);
            holder.tvDate = convertView.findViewById(R.id.tvDate);
            holder.btnDeleteBooking = convertView.findViewById(R.id.btnDeleteBooking);
            holder.btnBookingDetails = convertView.findViewById(R.id.btnBookingDetails);
            convertView.setTag(holder);
        } else {
            // If the view is being recycled, retrieve the ViewHolder from the tag
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the Booking object at the current position
        Booking booking = bookings.get(position);
        if (booking != null) {
            // Bind the Booking data to the views in the layout
            holder.tvPassengerName.setText(booking.getPassengerName());
            String route = booking.getDeparture() + " - " + booking.getDestination();
            holder.tvRoute.setText(route);
            holder.tvTicketType.setText(booking.getTicketType());

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            String formattedDate = sdf.format(booking.getDate());
            holder.tvDate.setText(formattedDate);

            // Set up click listeners for the delete and details buttons
            holder.btnDeleteBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Trigger the onDeleteClicked method in the listener, passing the Booking object
                    if (listener != null) {
                        listener.onDeleteClicked(booking);
                    }
                }
            });

            holder.btnBookingDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Trigger the onDetailsClicked method in the listener, passing the Booking object
                    if (listener != null) {
                        listener.onDetailsClicked(booking);
                    }
                }
            });
        }

        // Return the populated view
        return convertView;
    }

    // ViewHolder pattern to optimize view recycling
    private static class ViewHolder {
        TextView tvPassengerName;
        TextView tvRoute;
        TextView tvTicketType;
        TextView tvDate;
        Button btnDeleteBooking;
        Button btnBookingDetails;
    }

    // Method to set the listener for handling button click events
    public void setListener(BookingAdapterListener listener) {
        this.listener = listener;
    }
}
