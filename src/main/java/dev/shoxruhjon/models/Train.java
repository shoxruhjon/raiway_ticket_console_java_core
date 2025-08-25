package dev.shoxruhjon.models;

import dev.shoxruhjon.util.IdGenerator;

import java.time.LocalDateTime;

public class Train {
    private final String id;
    private final String from;
    private final String to;
    private final double price;
    private int availableSeats;
    private final LocalDateTime departureTime;

    public Train(String from, String to, double price, int availableSeats, LocalDateTime departureTime) {
        this.id = IdGenerator.generateId();
        this.from = from;
        this.to = to;
        this.price = price;
        this.availableSeats = availableSeats;
        this.departureTime = departureTime;
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void decreaseSeat() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    public void increaseSeat() {
        availableSeats++;
    }

}
