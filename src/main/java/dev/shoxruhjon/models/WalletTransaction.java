package dev.shoxruhjon.models;

import dev.shoxruhjon.utils.IdGenerator;

import java.time.LocalDateTime;

public class WalletTransaction {
    private final String id;
    private final String userId;
    private final double amount;
    private final String type;
    private final LocalDateTime timestamp;

    public WalletTransaction(String userId, double amount, String type) {
        this.id = IdGenerator.generateId();
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
