package dev.shoxruhjon.services;

import dev.shoxruhjon.models.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TrainService implements ITrainService {

    private final List<Train> trains;

    public TrainService(List<Train> trains) {
        this.trains = trains;
    }

    @Override
    public void seedTestTrains() {
        trains.add(new Train("Toshkent", "Samarqand", 100_000, 15, LocalDateTime.now().plusMinutes(30)));
        trains.add(new Train("Toshkent", "Buxoro", 120_000, 8, LocalDateTime.now().plusHours(2)));
        trains.add(new Train("Samarqand", "Toshkent", 100_000, 12, LocalDateTime.now().plusHours(4)));
        trains.add(new Train("Toshkent", "Andijon", 150_000, 5, LocalDateTime.now().plusHours(6)));
        trains.add(new Train("Buxoro", "Toshkent", 120_000, 10, LocalDateTime.now().plusHours(8)));

        trains.add(new Train("Toshkent", "Namangan", 110_000, 20, LocalDateTime.now().plusHours(10)));
        trains.add(new Train("Namangan", "Toshkent", 110_000, 18, LocalDateTime.now().plusHours(12)));
        trains.add(new Train("Samarqand", "Buxoro", 95_000, 14, LocalDateTime.now().plusHours(14)));
        trains.add(new Train("Buxoro", "Samarqand", 95_000, 16, LocalDateTime.now().plusHours(16)));
        trains.add(new Train("Andijon", "Toshkent", 150_000, 6, LocalDateTime.now().plusHours(18)));

        trains.add(new Train("Toshkent", "Xiva", 200_000, 9, LocalDateTime.now().plusHours(20)));
        trains.add(new Train("Xiva", "Toshkent", 200_000, 11, LocalDateTime.now().plusHours(22)));
        trains.add(new Train("Samarqand", "Andijon", 130_000, 7, LocalDateTime.now().plusHours(24)));
        trains.add(new Train("Andijon", "Samarqand", 130_000, 8, LocalDateTime.now().plusHours(26)));
        trains.add(new Train("Toshkent", "Qo‘qon", 140_000, 13, LocalDateTime.now().plusHours(28)));
    }


    @Override
    public List<Train> getTrains() {
        return trains;
    }

    @Override
    public Optional<Train> findById(String id) {
        return trains.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public void printUpcomingTop5() {
        System.out.println("\n=== POYEZD BILET TIZIMI ===\n");
        System.out.println("🕒 Yaqinlashib kelayotgan 5 ta reys:");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm (dd-MM-yyyy)");
        LocalDate today = LocalDate.now();

        List<Train> upcoming = trains.stream()
                .filter(t -> t.getDepartureTime().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Train::getDepartureTime))
                .limit(5)
                .toList();

        if (upcoming.isEmpty()) {
            System.out.println("❌ Hozircha yaqin reyslar mavjud emas.");
            return;
        }

        for (int i = 0; i < upcoming.size(); i++) {
            Train t = upcoming.get(i);
            String datePart = t.getDepartureTime().toLocalDate().isEqual(today)
                    ? t.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " (Bugun)"
                    : t.getDepartureTime().format(dtf);

            System.out.printf("%d. %s -> %s - %s - %,d so'm - %d ta joy%n",
                    i + 1, t.getFrom(), t.getTo(), datePart,
                    Math.round(t.getPrice()), t.getAvailableSeats());
        }
    }

    @Override
    public void printAll() {
        if (trains.isEmpty()) {
            System.out.println("🚂 Hozircha reyslar mavjud emas.");
            return;
        }

        List<Train> sorted = trains.stream()
                .sorted(Comparator.comparing(Train::getDepartureTime))
                .toList();

        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) sorted.size() / pageSize);
        int currentPage = 0;
        Scanner sc = new Scanner(System.in);

        while (true) {
            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, sorted.size());

            System.out.println("\n--- Reyslar ro‘yxati (sahifa " + (currentPage + 1) + "/" + totalPages + ") ---");
            for (int i = start; i < end; i++) {
                System.out.println((i + 1) + ". " + sorted.get(i));
            }

            StringBuilder menu = new StringBuilder("\n");
            if (currentPage > 0) menu.append("[p] Oldingi sahifa  ");
            if (currentPage < totalPages - 1) menu.append("[n] Keyingi sahifa  ");
            menu.append("[0] Chiqish");

            System.out.println(menu);
            System.out.print("Tanlov: ");
            String choice = sc.nextLine();

            if (choice.equals("n") && currentPage < totalPages - 1) {
                currentPage++;
            } else if (choice.equals("p") && currentPage > 0) {
                currentPage--;
            } else if (choice.equals("0")) {
                break;
            } else {
                System.out.println("❌ Noto‘g‘ri tanlov!");
            }
        }
    }

    @Override
    public Train selectTrain() {
        if (trains.isEmpty()) {
            System.out.println("🚂 Hozircha reyslar mavjud emas.");
            return null;
        }

        List<Train> sorted = trains.stream()
                .sorted(Comparator.comparing(Train::getDepartureTime))
                .toList();

        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) sorted.size() / pageSize);
        int currentPage = 0;
        Scanner sc = new Scanner(System.in);

        while (true) {
            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, sorted.size());

            System.out.println("\n--- Reyslar ro‘yxati (sahifa " + (currentPage + 1) + "/" + totalPages + ") ---");
            for (int i = start; i < end; i++) {
                System.out.println((i + 1) + ". " + sorted.get(i));
            }

            StringBuilder menu = new StringBuilder("\n");
            if (currentPage > 0) menu.append("[p] Oldingi sahifa  ");
            if (currentPage < totalPages - 1) menu.append("[n] Keyingi sahifa  ");
            menu.append("[0] Ortga  |  [raqam] Reys tanlash");

            System.out.println(menu);
            System.out.print("Tanlov: ");
            String input = sc.nextLine();

            if (input.equals("n") && currentPage < totalPages - 1) {
                currentPage++;
            } else if (input.equals("p") && currentPage > 0) {
                currentPage--;
            } else if (input.equals("0")) {
                System.out.println("↩️ Ortga qaytildi.");
                return null;
            } else {
                try {
                    int choice = Integer.parseInt(input);
                    if (choice < 1 || choice > sorted.size()) {
                        System.out.println("❌ Noto‘g‘ri raqam!");
                    } else {
                        return sorted.get(choice - 1);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Noto‘g‘ri qiymat!");
                }
            }
        }
    }

}
