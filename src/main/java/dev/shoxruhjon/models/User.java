package dev.shoxruhjon.models;

public class User extends BaseEntity {
    private final String username;
    private final String passwordHash;
    private final String fullName;
    private double walletBalance;

    public User(String fullName, String username, String passwordHash) {
        super();
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.walletBalance = 0.0;
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
        touch();
    }

    public void deductWalletBalance(double amount) {
        walletBalance -= amount;
        touch();
    }
}
