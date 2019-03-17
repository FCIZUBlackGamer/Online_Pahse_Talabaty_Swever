package talabaty.swever.com.online.Cart.Models;

public class Regions {
    int Id , CityId;
    String Name;

    public Regions(int id, int CityId, String name) {
        Id = id;
        Name = name;
        this.CityId = CityId;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public int getCityId() {
        return CityId;
    }
}
