package dev.shoxruhjon.services;

import dev.shoxruhjon.models.Ticket;
import dev.shoxruhjon.models.Train;
import dev.shoxruhjon.models.User;
import dev.shoxruhjon.models.WalletTransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketService {

    private final List<Ticket> tickets;
    private final List<WalletTransaction> transactions;

    public TicketService(List<Ticket> tickets, List<WalletTransaction> transactions) {
        this.tickets = tickets;
        this.transactions = transactions;
    }

    public boolean bookTicket(User user, Train train) {
        if(train.getAvailableSeats() <= 0) return false;
        if(user.getWalletBalance() < train.getPrice()) return false;

        user.deductWalletBalance(train.getPrice());
        transactions.add(new WalletTransaction(user.getId(), -train.getPrice(), "TICKET_PURCHASE"));

        tickets.add(new Ticket(train.getId(), user.getId(), train.getPrice()));
        train.decreaseSeat();
        return true;
    }

    public List<Ticket> getUserActiveTickets(String userId) {
        return tickets.stream()
                .filter(t -> t.getUserId().equals(userId) && !t.isCancelled())
                .collect(Collectors.toList());
    }

    public Optional<Ticket> findById(String id) {
        return tickets.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    /** Bekor qilish: jo'nashga 1 soatdan ko‘p vaqt qolganda ruxsat beriladi */
    public CancelResult cancelTicket(User user, Train train, Ticket ticket) {
        if (ticket.isCancelled()) {
            return new CancelResult(false, "Bu chipta allaqachon bekor qilingan.");
        }
        LocalDateTime limit = train.getDepartureTime().minusHours(1);
        if (limit.isBefore(LocalDateTime.now())) {
            return new CancelResult(false, "Bekor qilib bo'lmaydi: jo'nashga 1 soatdan kam vaqt qoldi.");
        }
        ticket.cancel();
        user.addWalletBalance(ticket.getPrice());
        transactions.add(new WalletTransaction(user.getId(), ticket.getPrice(), "TICKET_REFUND"));
        train.increaseSeat();
        return new CancelResult(true, "Chipta bekor qilindi va pul walletga qaytarildi.");
    }

    public static class CancelResult {
        public final boolean ok;
        public final String message;

        public CancelResult(boolean ok, String message) {
            this.ok = ok;
            this.message = message;
        }
    }

}
