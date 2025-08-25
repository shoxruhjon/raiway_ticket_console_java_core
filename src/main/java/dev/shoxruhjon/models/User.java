package dev.shoxruhjon.models;

import dev.shoxruhjon.util.IdGenerator;

public class User {
    private final String id;
    private final String username;
    private final String passwordHash;
    private final String fullName;
    private double walletBalance;

    public User(String username, String passwordHash, String fullName) {
        this.id = IdGenerator.generateId();
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.walletBalance = 0.0;
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public String getFullName() {
        return this.fullName;
    }

    public double getWalletBalance() {
        return this.walletBalance;
    }

    public void addWalletBalance(double amount) {
        this.walletBalance += amount;
    }

    public void deductWalletBalance(double amount) {
        this.walletBalance -= amount;
    }
}
