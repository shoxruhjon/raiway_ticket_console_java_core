package dev.shoxruhjon.services;

import dev.shoxruhjon.models.Ticket;
import dev.shoxruhjon.models.Train;
import dev.shoxruhjon.models.User;
import dev.shoxruhjon.models.WalletTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketService implements ITicketService {

    private final List<Ticket> tickets;
    private final List<WalletTransaction> transactions;

    public TicketService(List<Ticket> tickets, List<WalletTransaction> transactions) {
        this.tickets = tickets;
        this.transactions = transactions;
    }

    @Override
    public boolean bookTicket(User user, Train train) {
        if (train.getAvailableSeats() <= 0) return false;

        BigDecimal price = train.getPrice(); // Train da BigDecimal
        if (user.getWalletBalance().compareTo(price) < 0) {
            return false; // balans yetarli emas
        }

        // Pul yechib tashlanadi
        user.deductWalletBalance(price);

        // Transaction yoziladi (price.negate() - xarajatni ko‘rsatadi)
        transactions.add(new WalletTransaction(user.getId(), price.negate(), "TICKET_PURCHASE"));

        // Ticket yaratib qo‘shamiz
        tickets.add(new Ticket(train.getId(), user.getId(), price));

        // Joy sonini kamaytiramiz
        train.decreaseSeat();

        return true;
    }


    @Override
    public List<Ticket> getUserActiveTickets(String userId) {
        return tickets.stream()
                .filter(t -> t.getUserId().equals(userId) && !t.isCancelled())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Ticket> findById(String id) {
        return tickets.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public CancelResult cancelTicket(User user, Train train, Ticket ticket) {
        if (ticket.isCancelled()) {
            return new CancelResult(false, "Bu chipta allaqachon bekor qilingan.");
        }
        LocalDateTime limit = train.getDepartureTime().minusHours(1);
        if (limit.isBefore(LocalDateTime.now())) {
            return new CancelResult(false, "Bekor qilib bo'lmaydi: jo'nashga 1 soatdan kam vaqt qoldi.");
        }

        ticket.cancel();
        user.addToWallet(ticket.getPrice());
        transactions.add(new WalletTransaction(user.getId(), ticket.getPrice(), "TICKET_REFUND"));
        train.increaseSeat();
        return new CancelResult(true, "Chipta bekor qilindi va pul walletga qaytarildi.");
    }
}
