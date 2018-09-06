package talabaty.swever.com.online.Chart;

public class Sanf {
    float id;
    String name, image, state, color, imageId;
    float amount;

    public Sanf() {
    }

    public Sanf(float id, String name, String image, String state, String color, float amount) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.state = state;
        this.color = color;
        this.amount = amount;
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

    public void setState(String state) {
        this.state = state;
    }

    public float getId() {
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
        return state;
    }
}
