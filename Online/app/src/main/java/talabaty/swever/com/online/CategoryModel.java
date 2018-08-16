package talabaty.swever.com.online;

public class CategoryModel {
    int id;
    String name;
    String image;

    public CategoryModel(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
