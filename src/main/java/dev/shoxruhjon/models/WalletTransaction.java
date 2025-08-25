package dev.shoxruhjon.models;

import dev.shoxruhjon.util.IdGenerator;

import java.time.LocalDate;
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

    public String getId() { return this.id; }
    public String getUserId() { return this.userId; }
    public double getAmount() { return this.amount; }
    public String getType() { return this.type; }
    public LocalDateTime getTimestamp() { return this.timestamp; }
}
