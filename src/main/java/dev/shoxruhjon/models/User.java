package dev.shoxruhjon.models;

import dev.shoxruhjon.utils.IdGenerator;

public class User {
    private final String id;
    private final String username;
    private final String passwordHash;
    private final String fullName;
    private double walletBalance;

    public User(String fullName, String username, String passwordHash) {
        this.id = IdGenerator.generateId();
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.walletBalance = 0.0;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void addToWallet(double amount) {
        walletBalance += amount;
    }

    public void deductWalletBalance(double amount) {
        walletBalance -= amount;
    }
}
