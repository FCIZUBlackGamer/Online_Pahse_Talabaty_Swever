package talabaty.swever.com.online.WorkWithUs;

public class WorkWithUsModel {
    public int UserId ;
    public int CountryId ;
    public int CategoryId ;
    public int PackagesValueId ;
    public String Phone ;
    public String Name ;
    public int PhotoType ;
    public long IdentityId ;
    public String Photo ;
    public String EndDate ;
    public String Address ;
    public boolean DelivaryType ;

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setId(int id) {
        UserId = id;
    }

    public void setPhotoType(int photoType) {
        PhotoType = photoType;
    }

    public void setIdentityId(long identityId) {
        IdentityId = identityId;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public void setDelivaryType(boolean delivaryType) {
        DelivaryType = delivaryType;
    }


    public void setPackagesValueId(int packagesValueId) {
        PackagesValueId = packagesValueId;
    }
}
