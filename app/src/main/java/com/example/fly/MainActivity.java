package com.example.fly;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etDestination;
    private EditText etPassengerName;
    private EditText etDeparture;
    private EditText etDate;
    private Spinner spinnerTicketType;
    private Button btnBookTicket;
    private ArrayAdapter<CharSequence> ticketTypesAdapter; // Adapter for the spinner
    private Calendar calendar; // Date and time related variables
    private Date selectedDate;
    private Handler handler;  // Handler for delayed operations
    private Runnable dismissDialogRunnable;
    private TextView tvPrice;
    private double ticketPrice;

    private ToggleButton toggleBaggage;
    private boolean isBaggageChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        etDestination = findViewById(R.id.etDestination);
        etPassengerName = findViewById(R.id.etPassengerName);
        etDeparture = findViewById(R.id.etDeparture);
        etDate = findViewById(R.id.etDate);
        tvPrice = findViewById(R.id.tvPrice);
        toggleBaggage = findViewById(R.id.toggleBaggage);

        spinnerTicketType = findViewById(R.id.spinnerTicketType);
        btnBookTicket = findViewById(R.id.btnBookTicket);

        updateTicketPrice(); // Update ticket price based on user input

        // Set up the spinner with ticket types
        ticketTypesAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.ticket_types,
                android.R.layout.simple_spinner_item
        );

        ticketTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTicketType.setAdapter(ticketTypesAdapter);
        spinnerTicketType.setOnItemSelectedListener(this);

        calendar = Calendar.getInstance();

        // Set up date picker dialog when the date EditText is clicked
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Book ticket when the Book Ticket button is clicked
        btnBookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String destination = etDestination.getText().toString();
                String passengerName = etPassengerName.getText().toString();
                String departure = etDeparture.getText().toString();
                String ticketType = spinnerTicketType.getSelectedItem().toString();

                // Book the ticket with provided information
                bookTicket(destination, passengerName, departure, selectedDate, ticketType);
            }
        });

        // // Set up handler and runnable to show error dialog for 1.6 seconds
        handler = new Handler();
        dismissDialogRunnable = new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_error, null);

                TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
                Button dialogButton = dialogView.findViewById(R.id.dialogButton);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                }, 1600); // The dialog will be dismissed after 1.6 seconds
            }
        };


        // Update the ticket price when the baggage toggle button is checked/unchecked
        toggleBaggage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBaggageChecked = isChecked;
                updateTicketPrice();
            }
        });
    }

    // Check if baggage is checked
    public boolean isBaggageChecked() {

        return isBaggageChecked;
    }


    // Method to show date picker dialog
    private void showDatePicker() {
        Calendar currentDate = Calendar.getInstance(); // Calendar object to set minimum date in the date picker
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the date picker dialog
        // The selected date will be displayed in the etDate EditText
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        selectedDate = calendar.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                        etDate.setText(dateFormat.format(selectedDate));
                        updateTicketPrice();
                    }
                },
                year, month, day);

        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }


    // Method to update the ticket price based on user input
    private void updateTicketPrice() {
        // Get user input
        String destination = etDestination.getText().toString().trim();
        String departure = etDeparture.getText().toString().trim();

        // Calculate the ticket price based on user input and show it in the tvPrice TextView
        if (!destination.isEmpty() && !departure.isEmpty() && selectedDate != null) {
            String ticketType = spinnerTicketType.getSelectedItem().toString();
            double basePrice = 100 + (int) (Math.random() * (300 + 1));

            if (ticketType.equals("Standard")) {
                ticketPrice = basePrice;
            } else if (ticketType.equals("Business")) {
                ticketPrice = basePrice + 200 + (int) (Math.random() * (200 + 1));
            } else if (ticketType.equals("Elite")) {
                ticketPrice = basePrice + 500 + (int) (Math.random() * (200 + 1));
            }

            if (isBaggageChecked) {
                ticketPrice += 150;  // Add 150 euros to the price when baggage is checked
            }

            tvPrice.setText(String.valueOf(ticketPrice) + " €");
            tvPrice.setVisibility(View.VISIBLE);
        } else {
            tvPrice.setVisibility(View.GONE);
        }
    }


    // Method to book the ticket with provided information
    private void bookTicket(String destination, String passengerName, String departure, Date date, String ticketType) {
        // Check if all required fields are filled
        // If not, show an error dialog using the handler
        // Otherwise, proceed with booking and add the booking to the database
        // Display success or error toast based on the result of adding the booking
        if (passengerName.isEmpty() || departure.isEmpty() || destination.isEmpty() || date == null || ticketType.isEmpty()) {
            handler.removeCallbacks(dismissDialogRunnable);
            handler.post(dismissDialogRunnable);
            return; // Stop the method execution if any of the required fields is empty
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Booking booking = new Booking(0, passengerName, departure, destination, date, ticketType);
        long result = databaseHelper.addBooking(booking);

        if (result != -1) {
            Toast.makeText(this, "Reservations added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error when adding a booking", Toast.LENGTH_SHORT).show();
        }

        etDestination.setText("");
        etPassengerName.setText("");
        etDeparture.setText("");
        etDate.setText("");
        spinnerTicketType.setSelection(0);
    }


    // Callback method for item selection in the spinner
    // Update the ticket price and display a toast showing the selected ticket type
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedTicketType = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, "Type of the ticket: " + selectedTicketType, Toast.LENGTH_SHORT).show();
        updateTicketPrice();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Callback method when nothing is selected in the spinner
    }
}
