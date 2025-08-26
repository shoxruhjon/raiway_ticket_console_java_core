package dev.shoxruhjon.services;

import dev.shoxruhjon.models.User;

public interface IWalletService {
    void topUp(User user, double amount);
    void printWallet(User user);
    void printHistory(String userId);
}
