package talabaty.swever.com.online;

public class UserModel {
    public String FirstName ;
    public String LastName ;
    public String Mail ;
    public Boolean Gender ;
    public int CountryId ;
    public String Password ;
    public String UserName ;
    public String Phone ;
    public String Photo ;
    public String DateOfBirth ;

    public UserModel() {
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setGender(Boolean gender) {
        Gender = gender;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
