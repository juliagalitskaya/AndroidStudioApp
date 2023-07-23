package com.example.fly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "bookings.db";
    private static final int DATABASE_VERSION = 2;

    // Table and column names
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PASSENGER_NAME = "passenger_name";
    private static final String COLUMN_DEPARTURE = "departure";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TICKET_TYPE = "ticket_type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "bookings" table with columns: id, passenger_name, departure, destination, date, ticket_type
        String createTableQuery = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PASSENGER_NAME + " TEXT, " +
                COLUMN_DEPARTURE + " TEXT, " +
                COLUMN_DESTINATION + " TEXT, " +
                COLUMN_DATE + " INTEGER, " + // Store date as INTEGER (milliseconds since epoch)
                COLUMN_TICKET_TYPE + " TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade when the version changes
        // In this case, add the "date" column to the "bookings" table if it's an upgrade from version 1 to version 2
        if (oldVersion < 2) {
            String alterTableQuery = "ALTER TABLE " + TABLE_BOOKINGS +
                    " ADD COLUMN " + COLUMN_DATE + " INTEGER";
            db.execSQL(alterTableQuery);
        }
    }

    // Method to add a new booking to the database
    public long addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSENGER_NAME, booking.getPassengerName());
        values.put(COLUMN_DEPARTURE, booking.getDeparture());
        values.put(COLUMN_DESTINATION, booking.getDestination());
        values.put(COLUMN_DATE, booking.getDate().getTime()); // Convert Date to milliseconds
        values.put(COLUMN_TICKET_TYPE, booking.getTicketType());

        // Insert the values into the "bookings" table and return the row ID
        return db.insert(TABLE_BOOKINGS, null, values);
    }

    // Method to delete a booking from the database
    public boolean deleteBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOKINGS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(booking.getId())});
        db.close();
        return result > 0; // Return true if the delete operation affected one or more rows
    }

    // Method to get all bookings from the database
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query all rows from the "bookings" table
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKINGS, null);

        if (cursor.moveToFirst()) {
            // Loop through the cursor and create Booking objects from each row
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String passengerName = cursor.getString(cursor.getColumnIndex(COLUMN_PASSENGER_NAME));
                String departure = cursor.getString(cursor.getColumnIndex(COLUMN_DEPARTURE));
                String destination = cursor.getString(cursor.getColumnIndex(COLUMN_DESTINATION));
                long dateInMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE));
                String ticketType = cursor.getString(cursor.getColumnIndex(COLUMN_TICKET_TYPE));

                Date date = new Date(dateInMillis);

                Booking booking = new Booking(id, passengerName, departure, destination, date, ticketType);
                bookings.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bookings; // Return the list of Booking objects
    }
}
