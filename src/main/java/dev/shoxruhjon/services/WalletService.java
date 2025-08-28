package dev.shoxruhjon.services;

import dev.shoxruhjon.models.User;
import dev.shoxruhjon.models.WalletTransaction;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class WalletService implements IWalletService {
    private final List<WalletTransaction> transactions;

    public WalletService(List<WalletTransaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public void topUp(User user, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ Summani to‘g‘ri kiriting.");
            return;
        }
        user.addToWallet(amount);
        transactions.add(new WalletTransaction(user.getId(), amount, "DEPOSIT"));
        System.out.println("✅ Balans to‘ldirildi.");
    }

    @Override
    public void printWallet(User user) {
        System.out.printf("💰 Balans: %,d so'm%n",
                user.getWalletBalance().longValueExact());
    }

    @Override
    public void printHistory(String userId){
        System.out.println("\n=== TO'LOVLAR TARIXI ===");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<WalletTransaction> my = transactions.stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());

        if (my.isEmpty()) {
            System.out.println("Tarix bo‘sh.");
            return;
        }
        for (WalletTransaction t : my) {
            System.out.printf("%s | %s | %,d so'm%n",
                    t.getTimestamp().format(dtf),
                    t.getType(),
                    t.getAmount().longValueExact());
        }
    }
}
