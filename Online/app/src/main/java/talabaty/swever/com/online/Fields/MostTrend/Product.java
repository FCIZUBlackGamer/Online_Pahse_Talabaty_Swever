package talabaty.swever.com.online.Fields.MostTrend;

public class Product {
    int id;
    String name, image_url;
    float price;
    float sell;
    float rate;
    int IsOffer;

    public Product(int id, String name, String image_url, float price, float sell, float rate) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.price = price;
        this.sell = sell;
        this.rate = rate;
    }

    public void setIsOffer(int isOffer) {
        IsOffer = isOffer;
    }

    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public float getSell() {
        return sell;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getName() {
        return name;
    }

    public float getRate() {
        return rate;
    }

    public int getIsOffer() {
        return IsOffer;
    }
}
