package dev.shoxruhjon;

import dev.shoxruhjon.models.Ticket;
import dev.shoxruhjon.models.Train;
import dev.shoxruhjon.models.User;
import dev.shoxruhjon.services.AuthService;
import dev.shoxruhjon.services.TicketService;
import dev.shoxruhjon.services.TrainService;
import dev.shoxruhjon.services.WalletService;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner in = new Scanner(System.in);

    // "Bazadagi" listlar:
    private final Database db = new Database();

    // Xizmatlar:
    private final AuthService authService = new AuthService(db.users);
    private final TrainService trainService = new TrainService(db.trains);
    private final TicketService ticketService = new TicketService(db.tickets, db.transactions);
    private final WalletService walletService = new WalletService(db.transactions);

    public Menu() {
        trainService.seedTestTrains();
    }

    public void start() {
        while (true) {
            // Har ekranda — yaqin 5 ta reys
            trainService.printUpcomingTop5();

            if (authService.getCurrentUser() == null) {
                // Login qilmagan foydalanuvchi menyusi
                System.out.println("\n=== MENYU ===");
                System.out.println("1. Ro'yxatdan o'tish");
                System.out.println("2. Tizimga kirish");
                System.out.println("3. Barcha reyslarni ko'rish");
                System.out.println("0. Dasturdan chiqish");
                System.out.print("\nTanlov kiriting: ");
                int choice = readInt();

                switch (choice) {
                    case 1 -> doRegister();
                    case 2 -> doLogin();
                    case 3 -> trainService.printAll();
                    case 0 -> {
                        System.out.println("Dasturdan chiqildi.");
                        return;
                    }
                    default -> System.out.println("❌ Noto‘g‘ri tanlov!");
                }
            } else {
                // Login qilingan foydalanuvchi menyusi
                System.out.println("\n=== MENYU ===");
                System.out.println("3. Barcha reyslarni ko'rish");
                System.out.println("4. Bilet band qilish");
                System.out.println("5. Mening biletlarim");
                System.out.println("6. Biletni bekor qilish");
                System.out.println("7. Mening walletim");
                System.out.println("8. Walletni to'ldirish");
                System.out.println("9. Wallet tarixi");
                System.out.println("10. Tizimdan chiqish (logout)");
                System.out.println("0. Dasturdan chiqish");
                System.out.print("\nTanlov kiriting: ");
                int choice = readInt();

                switch (choice) {
                    case 3 -> trainService.printAll();
                    case 4 -> doBook();
                    case 5 -> doMyTickets();
                    case 6 -> doCancel();
                    case 7 -> walletService.printWallet(authService.getCurrentUser());
                    case 8 -> doTopUp();
                    case 9 -> walletService.printHistory(authService.getCurrentUser().getId());
                    case 10 -> {
                        authService.logout();
                        System.out.println("✅ Tizimdan chiqildi.");
                    }
                    case 0 -> {
                        System.out.println("Dasturdan chiqildi.");
                        return;
                    }
                    default -> System.out.println("❌ Noto‘g‘ri tanlov!");
                }
            }
        }
    }

    private void doRegister() {
        System.out.print("F.I.Sh kiriting: ");
        String full = in.nextLine();
        System.out.print("Username kiriting: ");
        String username = in.nextLine();
        System.out.print("Parol kiriting: ");
        String pass = in.nextLine();

        boolean ok = authService.register(full, username, pass);
        if (ok) System.out.println("✅ Ro‘yhatdan o‘tish muvaffaqiyatli!");
        else System.out.println("❌ Bunday username allaqachon mavjud.");
    }

    private void doLogin() {
        System.out.print("Username: ");
        String username = in.nextLine();
        System.out.print("Parol: ");
        String pass = in.nextLine();

        if (authService.login(username, pass))
            System.out.println("✅ Tizimga kirish muvaffaqiyatli!");
        else
            System.out.println("❌ Login yoki parol noto‘g‘ri!");
    }

    private void doBook() {
        User u = authService.getCurrentUser();
        trainService.printAll();
        System.out.print("Reys ID (UUID) ni kiriting: ");
        String id = in.nextLine();
        trainService.findById(id).ifPresentOrElse(train -> {
            boolean ok = ticketService.bookTicket(u, train);
            if (ok) System.out.println("✅ Bilet muvaffaqiyatli band qilindi!");
            else System.out.println("❌ Balans yetarli emas yoki joylar tugagan.");
        }, () -> System.out.println("❌ Bunday reys topilmadi."));
    }

    private void doMyTickets() {
        User u = authService.getCurrentUser();
        List<Ticket> my = ticketService.getUserActiveTickets(u.getId());
        if (my.isEmpty()) {
            System.out.println("Sizda faol bilet yo‘q.");
            return;
        }
        System.out.println("\n=== MENING BILETLARIM ===");
        for (Ticket t : my) {
            Train tr = trainService.findById(t.getTrainId()).orElse(null);
            if (tr != null) {
                System.out.printf("🎟 %s | %s -> %s | %,d so'm | BiletID: %s%n",
                        tr.getDepartureTime().toString(), tr.getFrom(), tr.getTo(),
                        Math.round(t.getPrice()), t.getId());
            }
        }
    }

    private void doCancel() {
        User u = authService.getCurrentUser();
        doMyTickets();
        System.out.print("Bekor qilinadigan BiletID ni kiriting: ");
        String ticketId = in.nextLine();

        ticketService.findById(ticketId).ifPresentOrElse(t -> {
            if (!t.getUserId().equals(u.getId())) {
                System.out.println("❌ Bu bilet sizga tegishli emas.");
                return;
            }
            Train tr = trainService.findById(t.getTrainId()).orElse(null);
            if (tr == null) {
                System.out.println("❌ Tegishli reys topilmadi.");
                return;
            }
            TicketService.CancelResult res = ticketService.cancelTicket(u, tr, t);
            System.out.println((res.ok ? "✅ " : "❌ ") + res.message);
        }, () -> System.out.println("❌ Bunday BiletID topilmadi."));
    }

    private void doTopUp() {
        User u = authService.getCurrentUser();
        System.out.print("To‘ldirish summasini kiriting: ");
        double amount = readDouble();
        walletService.topUp(u, amount);
        walletService.printWallet(u);
    }

    private int readInt() {
        try {
            String s = in.nextLine().trim();
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

    private double readDouble() {
        try {
            String s = in.nextLine().trim();
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
