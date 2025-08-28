package dev.shoxruhjon.models;

import java.math.BigDecimal;

public class User extends BaseEntity {
    private final String username;
    private final String passwordHash;
    private final String fullName;
    private BigDecimal walletBalance;

    public User(String fullName, String username, String passwordHash) {
        super();
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.walletBalance = BigDecimal.ZERO;
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

    public BigDecimal getWalletBalance() {
        return walletBalance;
    }

    public void addToWallet(BigDecimal amount) {
        walletBalance = walletBalance.add(amount);
        touch();
    }

    public void deductWalletBalance(BigDecimal amount) {
        walletBalance = walletBalance.subtract(amount);
        touch();
    }
}
