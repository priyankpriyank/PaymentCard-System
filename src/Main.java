import java.io.*;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "data.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your User ID (or -1 to create a new user): ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (userId == -1) {
            System.out.print("Enter your name: ");
            String userName = scanner.nextLine();
            userId = createNewUser(userName);
            System.out.println("Your new User ID is: " + userId);
        } else if (!userExists(userId)) {
            System.out.println("User ID not found. Exiting...");
            return;
        }

        PaymentCard userCard = new PaymentCard(userId);
        PaymentTerminal terminal = new PaymentTerminal();

        while (true) {
            System.out.println("\n1. Eat affordably (12.50 Dollars)");
            System.out.println("2. Eat heartily (20.30 Dollars)");
            System.out.println("3. Add money to card");
            System.out.println("4. Check balance");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println(terminal.eatAffordably(userCard) ? "Meal purchased!" : "Insufficient funds!");
                    break;
                case 2:
                    System.out.println(terminal.eatHeartily(userCard) ? "Meal purchased!" : "Insufficient funds!");
                    break;
                case 3:
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    userCard.addMoney(amount);
                    System.out.println("Money added successfully!");
                    break;

                case 4:
                    double money = userCard.getBalance();
                    System.out.print("Balance: "+ String.format("%.2f", money) +" Dollars");
                    System.out.println();

                    break;
                case 5:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static boolean userExists(int userId) {
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(userId + ",")) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static int createNewUser(String name) {
        int newUserId = 1;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            bw.write(newUserId + "," + name + ",0.0\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newUserId;
    }
}
