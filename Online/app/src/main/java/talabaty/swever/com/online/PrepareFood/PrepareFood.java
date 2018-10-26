package talabaty.swever.com.online.PrepareFood;

public class PrepareFood {
    int id;
    String name, image;
    float price;


    public PrepareFood(int id, String name, String image, float price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public float getPrice() {
        return price;
    }
}
