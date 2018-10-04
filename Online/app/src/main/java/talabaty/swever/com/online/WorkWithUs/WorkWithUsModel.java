package talabaty.swever.com.online.WorkWithUs;

public class WorkWithUsModel {
    public int Id ;
    public String FirstName ;
    public String LastName ;
    public String Mail ;
    public boolean Gender ;
    public int CountryId ;
    public String Password ;
    public String UserName ;
    public String Phone ;
    public int PhotoType ;
    public int AccountType ;
    public long IdentityId ;
    public String Photo ;
    public String EndDate ;
    public String Address ;
    public String DateOfBirth ;
    public int Shop_Id ;
    public int Rule_Id ;
    public int RegionId ;
    public boolean IsVerified ;
    public boolean Block ;

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setGender(boolean gender) {
        Gender = gender;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setVerified(boolean verified) {
        IsVerified = verified;
    }

    public void setRule_Id(int rule_Id) {
        Rule_Id = rule_Id;
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

    public void setBlock(boolean block) {
        Block = block;
    }

    public void setAccountType(int accountType) {
        AccountType = accountType;
    }

    public void setShop_Id(int shop_Id) {
        Shop_Id = shop_Id;
    }

    public void setRegionId(int regionId) {
        RegionId = regionId;
    }
}
