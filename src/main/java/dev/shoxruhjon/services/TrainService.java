package dev.shoxruhjon.services;

import dev.shoxruhjon.models.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TrainService {

    private final List<Train> trains;

    public TrainService(List<Train> trains) {
        this.trains = trains;
    }

    public void seedTestTrains() {
        trains.add(new Train("Toshkent", "Samarqand", 100_000, 15, LocalDateTime.now().plusMinutes(30)));
        trains.add(new Train("Toshkent", "Buxoro", 120_000, 8, LocalDateTime.now().plusHours(2)));
        trains.add(new Train("Samarqand", "Toshkent", 100_000, 12, LocalDateTime.now().plusHours(4)));
        trains.add(new Train("Toshkent", "Andijon", 150_000, 5, LocalDateTime.now().plusHours(6)));
        trains.add(new Train("Buxoro", "Toshkent", 120_000, 10, LocalDateTime.now().plusHours(8)));
    }

    public List<Train> getTrains() {
        return trains;
    }

    public Optional<Train> findById(long id) {
        return trains.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    public void printUpcomingTop5() {
        System.out.println("\n=== POYEZD BILET TIZIMI ===\n");
        System.out.println("🕒 Yaqinlashib kelayotgan 5 ta reys:");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm (dd-MM-yyyy)");
        LocalDate today = LocalDate.now();

        trains.stream()
                .filter(t -> t.getDepartureTime().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Train::getDepartureTime))
                .limit(5)
                .forEach(t -> {
                    String datePart = t.getDepartureTime().toLocalDate().isEqual(today)
                            ? t.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " (Bugun)"
                            : t.getDepartureTime().format(dtf);
                    System.out.printf("%s. %s -> %s - %s - %,d so'm - %d ta joy%n",
                            t.getId(), t.getFrom(), t.getTo(), datePart, Math.round(t.getPrice()), t.getAvailableSeats());
                });
    }

    public void printAll() {
        System.out.println("\n=== BARCHA REYSLAR ===");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm (dd-MM-yyyy)");
        trains.stream()
                .sorted(Comparator.comparing(Train::getDepartureTime))
                .forEach(t -> System.out.printf("%s. %s -> %s - %s - %,d so'm - %d ta joy%n",
                        t.getId(), t.getFrom(), t.getTo(),
                        t.getDepartureTime().format(dtf),
                        Math.round(t.getPrice()), t.getAvailableSeats()));
    }
}
