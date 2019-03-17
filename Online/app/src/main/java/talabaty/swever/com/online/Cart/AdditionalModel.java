package talabaty.swever.com.online.Cart;

import java.io.Serializable;

public class AdditionalModel implements Serializable{
    public int Id;
    public String Name;
    public double Price;
    boolean isChecked = false;

    public AdditionalModel(int id, String name, double price) {
        Id = id;
        Name = name;
        Price = price;
    }
    public AdditionalModel(String id, String name, String price) {
        Id = Integer.parseInt(id);
        Name = name;
        Price = Double.parseDouble(price);
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getPrice() {
        return Price;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }
}
