package dev.shoxruhjon.models;

import dev.shoxruhjon.util.IdGenerator;

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
        return this.id;
    }

    public String getTrainId() {
        return this.trainId;
    }

    public String getUserId() {
        return this.userId;
    }

    public LocalDateTime getPurchaseTime() {
        return this.purchaseTime;
    }

    public double getPrice() {
        return this.price;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
