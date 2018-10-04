package talabaty.swever.com.online.WorkWithUs;

public class spin {
    int id;
    int value;
    String name;

    public spin(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public spin(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
