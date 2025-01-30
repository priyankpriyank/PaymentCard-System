import java.io.*;
import java.util.Date;

class PaymentTerminal {
    private static final String DATA_FILE = "data.txt";

    public boolean eatAffordably(PaymentCard card) {
        double price = 12.50;
        if (card.takeMoney(price)) {
            recordSale("AFFORDABLE", price);
            return true;
        }
        return false;
    }

    public boolean eatHeartily(PaymentCard card) {
        double price = 20.30;
        if (card.takeMoney(price)) {
            recordSale("HEARTY", price);
            return true;
        }
        return false;
    }

    private void recordSale(String mealType, double price) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            bw.write( "SALE," + mealType + "," + price + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
