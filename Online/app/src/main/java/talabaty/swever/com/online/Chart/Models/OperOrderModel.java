package talabaty.swever.com.online.Chart.Models;

import java.io.Serializable;

public class OperOrderModel implements Serializable{
    int Id;
    int Amount;
    double Price;
    String name;

    public OperOrderModel(int id, String nae, int amount, double price) {
        Id = id;
        Amount = amount;
        Price = price;
        name = nae;
    }

    public OperOrderModel(String nae, int amount, double price) {
        Amount = amount;
        Price = price;
        name = nae;
    }

    public int getId() {
        return Id;
    }

    public double getPrice() {
        return Price;
    }

    public int getAmount() {
        return Amount;
    }

    public String getName() {
        return name;
    }

}
