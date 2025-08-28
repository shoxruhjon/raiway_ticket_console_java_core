package dev.shoxruhjon;

import dev.shoxruhjon.models.Ticket;
import dev.shoxruhjon.models.Train;
import dev.shoxruhjon.models.User;
import dev.shoxruhjon.services.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner in = new Scanner(System.in);


    private final IAuthService authService;
    private final ITrainService trainService;
    private final ITicketService ticketService;
    private final IWalletService walletService;

    public Menu() {
        Database db = new Database();
        this.authService = new AuthService(db.users);
        this.trainService = new TrainService(db.trains);
        this.ticketService = new TicketService(db.tickets, db.transactions);
        this.walletService = new WalletService(db.transactions);

        trainService.seedTestTrains();
    }


    private boolean firstLaunch = true;
    private boolean showAfterLogin = false;


    public void start() {
        while (true) {

            if (firstLaunch || showAfterLogin) {
                trainService.printUpcomingTop5();
                firstLaunch = false;
                showAfterLogin = false;
            }

            if (authService.getCurrentUser() == null) {

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

                System.out.println("\n=== MENYU ===");
                System.out.println("1. Barcha reyslarni ko'rish");
                System.out.println("2. Bilet band qilish");
                System.out.println("3. Mening biletlarim");
                System.out.println("4. Biletni bekor qilish");
                System.out.println("5. Mening balansim");
                System.out.println("6. Balansni to'ldirish");
                System.out.println("7. To'lovlar tarixi");
                System.out.println("8. Tizimdan chiqish (logout)");
                System.out.println("0. Dasturdan chiqish");
                System.out.print("\nTanlov kiriting: ");
                int choice = readInt();

                switch (choice) {
                    case 1 -> trainService.printAll();
                    case 2 -> doBook();
                    case 3 -> doMyTickets();
                    case 4 -> doCancel();
                    case 5 -> walletService.printWallet(authService.getCurrentUser());
                    case 6 -> doTopUp();
                    case 7 -> walletService.printHistory(authService.getCurrentUser().getId());
                    case 8 -> {
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

        if (authService.login(username, pass)) {
            System.out.println("✅ Tizimga kirish muvaffaqiyatli!");
            showAfterLogin = true;
        } else {
            System.out.println("❌ Login yoki parol noto‘g‘ri!");
        }
    }

    private void doBook() {
        User u = authService.getCurrentUser();

        Train train = trainService.selectTrain();
        if (train == null) return;

        boolean ok = ticketService.bookTicket(u, train);

        if (ok)
            System.out.println("✅ Bilet muvaffaqiyatli band qilindi!");
        else
            System.out.println("❌ Balans yetarli emas yoki joylar tugagan.");
    }

    private void doMyTickets() {
        User u = authService.getCurrentUser();
        List<Ticket> my = ticketService.getUserActiveTickets(u.getId());

        if (my.isEmpty()) {
            System.out.println("❌ Sizda faol bilet yo‘q.");
            return;
        }

        System.out.println("\n=== MENING BILETLARIM ===");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < my.size(); i++) {
            Ticket t = my.get(i);
            Train tr = trainService.findById(t.getTrainId()).orElse(null);
            if (tr != null) {
                System.out.printf("%d. 🎟 %s | %s -> %s | %,d so'm%n",
                        i + 1,
                        tr.getDepartureTime().format(fmt),
                        tr.getFrom(),
                        tr.getTo(),
                        t.getPrice().setScale(0, RoundingMode.HALF_UP).longValue()
                );
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
        BigDecimal amount = new BigDecimal(readDouble());
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
