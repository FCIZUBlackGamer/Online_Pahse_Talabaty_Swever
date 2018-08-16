package talabaty.swever.com.online.NearestContacts;

public class ContactInfo {
    int Id, Visitor, CatogoryId, CityId, RegionId;
    float Rate;
    String Name;
    String Photo, Address, Phone, Descripation, Descripation1, Category, CityName, EnglishCityName, RegionName;

    public ContactInfo(int id,
                       int visitor,
                       int catogoryId,
                       int cityId,
                       int regionId,
                       float rate,
                       String name,
                       String photo,
                       String address,
                       String phone,
                       String descripation,
                       String descripation1,
                       String category,
                       String cityName,
                       String englishCityName,
                       String regionName) {
        Id = id;
        Visitor = visitor;
        CatogoryId = catogoryId;
        CityId = cityId;
        RegionId = regionId;
        Rate = rate;
        Name = name;
        Photo = photo;
        Address = address;
        Phone = phone;
        Descripation = descripation;
        Descripation1 = descripation1;
        Category = category;
        CityName = cityName;
        EnglishCityName = englishCityName;
        RegionName = regionName;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setRate(float rate) {
        Rate = rate;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    public void setRegionId(int regionId) {
        RegionId = regionId;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setCatogoryId(int catogoryId) {
        CatogoryId = catogoryId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public void setDescripation(String descripation) {
        Descripation = descripation;
    }

    public void setDescripation1(String descripation1) {
        Descripation1 = descripation1;
    }

    public void setEnglishCityName(String englishCityName) {
        EnglishCityName = englishCityName;
    }

    public void setVisitor(int visitor) {
        Visitor = visitor;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public float getRate() {
        return Rate;
    }

    public String getRegionName() {
        return RegionName;
    }

    public int getRegionId() {
        return RegionId;
    }

    public int getCatogoryId() {
        return CatogoryId;
    }

    public int getCityId() {
        return CityId;
    }

    public int getVisitor() {
        return Visitor;
    }

    public String getAddress() {
        return Address;
    }

    public String getCategory() {
        return Category;
    }

    public String getCityName() {
        return CityName;
    }

    public String getDescripation() {
        return Descripation;
    }

    public String getDescripation1() {
        return Descripation1;
    }

    public String getEnglishCityName() {
        return EnglishCityName;
    }

    public String getPhoto() {
        return Photo;
    }
}
