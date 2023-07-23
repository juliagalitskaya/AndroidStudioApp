package com.example.fly;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements BookingAdapterListener {

    private ListView listBookings;
    private Button btnBookTicket;

    // Data and Adapter
    private List<Booking> bookings;
    private BookingAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Find UI elements by their IDs
        listBookings = findViewById(R.id.listBookings);
        btnBookTicket = findViewById(R.id.btnBookTicket);

        // Fetch all bookings from the database
        bookings = databaseHelper.getAllBookings();

        // Set up the adapter and attach the listener for item clicks
        adapter = new BookingAdapter(this, bookings);
        adapter.setListener(this);
        listBookings.setAdapter(adapter);

        // Set up the "Book Ticket" button click listener
        btnBookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MainActivity when the button is clicked
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set up the item click listener for the ListView
        listBookings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle the click on a booking item and start BookingDetailsActivity
                Booking selectedBooking = bookings.get(position);
                Intent intent = new Intent(HomeActivity.this, BookingDetailsActivity.class);
                intent.putExtra("passenger_name", selectedBooking.getPassengerName());
                intent.putExtra("route", selectedBooking.getDeparture() + " - " + selectedBooking.getDestination());
                intent.putExtra("date", selectedBooking.getDate().getTime()); // Pass the date as a long value
                intent.putExtra("departure", selectedBooking.getDeparture());
                intent.putExtra("destination", selectedBooking.getDestination());
                intent.putExtra("ticket_type", selectedBooking.getTicketType());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDeleteClicked(Booking booking) {
        // Handle delete button click for a booking item
        // Delete the booking from the database and update the list
        boolean success = databaseHelper.deleteBooking(booking);
        if (success) {
            Toast.makeText(this, "Reservation deleted", Toast.LENGTH_SHORT).show();
            updateBookings();
        } else {
            Toast.makeText(this, "Error when deleting a booking", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetailsClicked(Booking booking) {
        // Handle details button click for a booking item
        // Start BookingDetailsActivity with details of the selected booking
        Intent intent = new Intent(HomeActivity.this, BookingDetailsActivity.class);
        intent.putExtra("booking_id", booking.getId());
        intent.putExtra("passenger_name", booking.getPassengerName());
        intent.putExtra("route", booking.getDeparture() + " - " + booking.getDestination());
        intent.putExtra("date", booking.getDate().getTime()); // Pass the date as a long value
        intent.putExtra("departure", booking.getDeparture());
        intent.putExtra("destination", booking.getDestination());
        intent.putExtra("ticket_type", booking.getTicketType());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the list of bookings when the activity is resumed
        updateBookings();
    }

    private void updateBookings() {
        // Update the list of bookings from the database and notify the adapter
        bookings.clear();
        bookings.addAll(databaseHelper.getAllBookings());
        adapter.notifyDataSetChanged();
    }
}
