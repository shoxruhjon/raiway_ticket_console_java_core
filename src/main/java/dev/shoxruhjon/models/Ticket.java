package dev.shoxruhjon.models;

import java.time.LocalDateTime;

public class Ticket extends BaseEntity {
    private final String trainId;
    private final String userId;
    private final LocalDateTime purchaseTime;
    private final double price;
    private boolean cancelled;

    public Ticket(String trainId, String userId, double price) {
        super();
        this.trainId = trainId;
        this.userId = userId;
        this.price = price;
        this.purchaseTime = LocalDateTime.now();
        this.cancelled = false;
    }

    public String getTrainId() {
        return trainId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public double getPrice() {
        return price;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
        touch();
    }
}
