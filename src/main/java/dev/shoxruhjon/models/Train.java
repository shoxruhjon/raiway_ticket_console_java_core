package dev.shoxruhjon.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Train extends BaseEntity {
    private final String from;
    private final String to;
    private final BigDecimal price;
    private int availableSeats;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;

    public Train(String from, String to, BigDecimal price, int availableSeats,
                 LocalDateTime departureTime, LocalDateTime arrivalTime) {
        super();
        this.from = from;
        this.to = to;
        this.price = price;
        this.availableSeats = availableSeats;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return String.format("%s -> %s | %s - %s | %s so'm | %d ta joy",
                from,
                to,
                departureTime.format(dtf),
                arrivalTime.format(dtf),
                price.toPlainString(),
                availableSeats);
    }
}
