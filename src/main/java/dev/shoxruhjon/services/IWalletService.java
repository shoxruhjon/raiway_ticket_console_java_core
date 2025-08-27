package dev.shoxruhjon.services;

import dev.shoxruhjon.models.User;

import java.math.BigDecimal;

public interface IWalletService {
    void topUp(User user, BigDecimal amount);
    void printWallet(User user);
    void printHistory(String userId);
}
