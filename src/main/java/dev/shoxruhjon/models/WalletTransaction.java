package dev.shoxruhjon.models;

import java.time.LocalDateTime;

public class WalletTransaction extends BaseEntity {
    private final String userId;
    private final double amount;
    private final String type;
    private final LocalDateTime timestamp;

    public WalletTransaction(String userId, double amount, String type) {
        super();
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
