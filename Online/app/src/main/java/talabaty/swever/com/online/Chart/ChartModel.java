package talabaty.swever.com.online.Chart;


import java.io.Serializable;
import java.util.List;

public class ChartModel implements Serializable{
    public int Id ;
    public int Amount ;
    public double Price ;
    public String Name ; // ProductName
    public String Image ; //null
    public int Size ;
    public int Color ;
    public int ShopId ; // Contact Id For That Product
    public double distance ;
    //0is not offer
    //1 is offer
    //2 is dash
    //3 is addition
    public int IsOffer ;
    public List<AdditionalModel> AdditionList ;

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

    public List<AdditionalModel> getAdditionList() {
        return AdditionList;
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

    public void setAdditionList(List<AdditionalModel> additionList) {
        AdditionList = additionList;
    }

    public void setIsOffer(int isOffer) {
        IsOffer = isOffer;
    }

    public int getIsOffer() {
        return IsOffer;
    }

    public int getId() {
        return Id;
    }
}
