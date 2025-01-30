import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class PaymentCard {
    private int userId;
    private String userName;
    private double balance;
    private static final String DATA_FILE = "data.txt";

    public PaymentCard(int userId) {
        this.userId = userId;
        loadUserData();
    }

    private void loadUserData() {
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            boolean userSection = false;
            while ((line = br.readLine()) != null) {
                if (line.equals("[USERS]")) {
                    userSection = true;
                    continue;
                } else if (line.startsWith("[")) {
                    userSection = false;
                }

                if (userSection) {
                    String[] data = line.split(",");
                    if (Integer.parseInt(data[0]) == userId) {
                        userName = data[1];
                        balance = Double.parseDouble(data[2]);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        balance = 0; // Default if user not found
    }

    private void saveUserData() {
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        boolean userSection = false;

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("[USERS]")) {
                    userSection = true;
                    lines.add(line);
                    continue;
                } else if (line.startsWith("[")) {
                    userSection = false;
                }

                if (userSection) {
                    String[] data = line.split(",");
                    if (Integer.parseInt(data[0]) == userId) {
                        lines.add(userId + "," + userName + "," + balance);
                        updated = true;
                        continue;
                    }
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!updated) {
            lines.add(userId + "," + userName + "," + balance);
        }

        try {
            Files.write(Paths.get(DATA_FILE), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getBalance() {
        return balance;
    }

    public void addMoney(double amount) {
        if (amount > 0) {
            balance += amount;
            saveUserData();
            recordTransaction("ADD_MONEY", amount);
        }
    }

    public boolean takeMoney(double amount) {
        if (balance >= amount) {
            balance -= amount;
            saveUserData();
            recordTransaction("BUY_MEAL", amount);
            return true;
        }
        return false;
    }

    private void recordTransaction(String type, double amount) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            bw.write(userId + "," + type + "," + amount + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
