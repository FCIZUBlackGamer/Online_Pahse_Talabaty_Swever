package talabaty.swever.com.online.Cart;

import java.util.List;

public class Sanf {
    int id;
    String name, image, size, color, imageId;
    float amount, price;
    //0is not offer
    //1 is offer
    //2 is dash
    //3 is addition
    public int IsOffer ;
    public List<AdditionalModel> AdditionList ;

    public Sanf() {
    }

    public Sanf(int id, String name, String image, String size, String color, float amount) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.size = size;
        this.color = color;
        this.amount = amount;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Sanf(int id, String name, float amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public Sanf(int id, String name, String photo, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = photo;
    }

    public float getPrice() {
        return price;
    }

    public void setIsOffer(int isOffer) {
        IsOffer = isOffer;
    }

    public int getIsOffer() {
        return IsOffer;
    }

    public void setAdditionList(List<AdditionalModel> additionList) {
        AdditionList = additionList;
    }

    public List<AdditionalModel> getAdditionList() {
        return AdditionList;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setState(String size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public String getColor() {
        return color;
    }

    public String getImage() {
        return image;
    }

    public String getState() {
        return size;
    }
}
