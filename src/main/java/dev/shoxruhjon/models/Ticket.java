package dev.shoxruhjon.models;

import dev.shoxruhjon.utils.IdGenerator;

import java.time.LocalDateTime;

public class Ticket {
    private final String id;
    private final String trainId;
    private final String userId;
    private final LocalDateTime purchaseTime;
    private final double price;
    private boolean cancelled;

    public Ticket(String trainId, String userId, double price) {
        this.id = IdGenerator.generateId();
        this.trainId = trainId;
        this.userId = userId;
        this.price = price;
        this.purchaseTime = LocalDateTime.now();
        this.cancelled = false;
    }

    public String getId() {
        return id;
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
        cancelled = true;
    }
}
