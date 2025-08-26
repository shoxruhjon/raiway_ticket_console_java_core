package dev.shoxruhjon.services;

import dev.shoxruhjon.models.Ticket;
import dev.shoxruhjon.models.Train;
import dev.shoxruhjon.models.User;

import java.util.List;
import java.util.Optional;

public interface ITicketService {
    boolean bookTicket(User user, Train train);
    List<Ticket> getUserActiveTickets(String userId);
    Optional<Ticket> findById(String id);
    CancelResult cancelTicket(User user, Train train, Ticket ticket);

    class CancelResult {
        public final boolean ok;
        public final String message;

        public CancelResult(boolean ok, String message) {
            this.ok = ok;
            this.message = message;
        }
    }
}
