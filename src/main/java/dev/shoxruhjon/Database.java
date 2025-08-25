package dev.shoxruhjon;

import dev.shoxruhjon.models.Ticket;
import dev.shoxruhjon.models.Train;
import dev.shoxruhjon.models.User;
import dev.shoxruhjon.models.WalletTransaction;

import java.util.ArrayList;
import java.util.List;

public class Database {
    public final List<User> users = new ArrayList<>();
    public final List<Ticket> tickets = new ArrayList<>();
    public final List<Train> trains = new ArrayList<>();
    public final List<WalletTransaction> transactions = new ArrayList<>();
}
