package com.example.fly;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingDetailsActivity extends AppCompatActivity {

    private TextView tvPassengerName;
    private TextView tvRoute;
    private TextView tvDate;
    private TextView tvDeparture;
    private TextView tvDestination;
    private TextView tvTicketType;
    private Button btnBack;
    private Button btnBackAdditional;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        // Initialize UI elements
        tvPassengerName = findViewById(R.id.tvPassengerName);
        tvRoute = findViewById(R.id.tvRoute);
        tvDate = findViewById(R.id.tvDate);
        tvDeparture = findViewById(R.id.tvDeparture);
        tvDestination = findViewById(R.id.tvDestination);
        tvTicketType = findViewById(R.id.tvTicketType);

        // Get data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            String passengerName = intent.getStringExtra("passenger_name");
            String route = intent.getStringExtra("route");
            long dateMillis = intent.getLongExtra("date", 0);
            String departure = intent.getStringExtra("departure");
            String destination = intent.getStringExtra("destination");
            String ticketType = intent.getStringExtra("ticket_type");

            Date date = new Date(dateMillis);

            // Set data to the TextViews
            tvPassengerName.setText(passengerName);
            tvRoute.setText(route);
            tvDate.setText(formatDate(date));
            tvDeparture.setText(departure);
            tvDestination.setText(destination);
            tvTicketType.setText(ticketType);

            // Set up click listeners for back buttons
            btnBack = findViewById(R.id.btnBack);
            btnBackAdditional = findViewById(R.id.btnBackAdditional);

            // Show booking confirmation dialog when the primary back button is clicked
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBookingConfirmation();
                }
            });

            // Finish the activity when the additional back button is clicked
            btnBackAdditional.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    // Helper method to format Date object as a String
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    // Method to show the booking confirmation dialog
    private void showBookingConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_booking_confirmation, null);

        TextView tvConfirmationMessage = dialogView.findViewById(R.id.tvConfirmationMessage);
        Button btnOK = dialogView.findViewById(R.id.btnOK);

        // Set the confirmation message (if needed)
        // tvConfirmationMessage.setText("Your ticket successfully booked");

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCancelable(false);

        // Set up the OK button click listener to dismiss the dialog and finish the activity
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        // Show the dialog and automatically dismiss it after 1.8 seconds
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    finish();
                }
            }
        }, 1800);
    }

    @Override
    protected void onDestroy() {
        // Dismiss the dialog if it is showing to avoid memory leaks
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}
