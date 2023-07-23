package com.example.fly;

import java.util.Date;

public class Booking {

    // Private fields to store booking information
    private int id;
    private String passengerName;
    private String departure;
    private String destination;
    private Date date; // Using Date data type to store the date
    private String ticketType;

    // Constructor to create a new Booking object with the provided details
    public Booking(int id, String passengerName, String departure, String destination, Date date, String ticketType) {
        this.id = id;
        this.passengerName = passengerName;
        this.departure = departure;
        this.destination = destination;
        this.date = date;
        this.ticketType = ticketType;
    }

    // Getter methods to access the booking information
    public int getId() {
        return id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDate() {
        return date;
    }

    public String getTicketType() {
        return ticketType;
    }

    // Setter method to update the booking date
    public void setDate(Date date) {
        this.date = date;
    }

    // Method to get the details of the booking as a formatted string
    public String getDetails() {
        return "Destination: " + destination +
                "\nPassenger name: " + passengerName +
                "\nDeparture: " + departure +
                "\nDate: " + date +
                "\nTicket type: " + ticketType;
    }
}

