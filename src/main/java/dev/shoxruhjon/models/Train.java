package dev.shoxruhjon.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Train extends BaseEntity {
    private final String from;
    private final String to;
    private final double price;
    private int availableSeats;
    private final LocalDateTime departureTime;

    public Train(String from, String to, double price, int availableSeats, LocalDateTime departureTime) {
        super();
        this.from = from;
        this.to = to;
        this.price = price;
        this.availableSeats = availableSeats;
        this.departureTime = departureTime;
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
            touch();
        }
    }

    public void increaseSeat() {
        availableSeats++;
        touch();
    }

    @Override
    public String toString() {
        return String.format("%s -> %s - %s - %,.0f so'm - %d ta joy",
                from,
                to,
                departureTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                price,
                availableSeats);
    }

}
