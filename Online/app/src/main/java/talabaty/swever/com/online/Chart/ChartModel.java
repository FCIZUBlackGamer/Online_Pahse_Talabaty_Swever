package talabaty.swever.com.online.Chart;


public class ChartModel {
    public int Id ;
    public int Amount ;
    public double Price ;
    public String Name ; // ProductName
    public String Image ; //null
    public int Size ;
    public int Color ;
    public int ShopId ; // Contact Id For That Product
    public double distance ;

    public ChartModel(){
        ShopId = 0;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setColor(int color) {
        Color = color;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public void setShopId(int shopId) {
        ShopId = shopId;
    }

    public void setSize(int size) {
        Size = size;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
