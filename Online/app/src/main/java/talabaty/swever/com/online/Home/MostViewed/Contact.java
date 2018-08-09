package talabaty.swever.com.online.Home.MostViewed;

public class Contact {
    int id;
    String name;
    float rate;
    String location;
    String email;
    String phone;
    String company_logo;
    String company_qrCode;

    public Contact(int id, String name, float rate, String location, String email, String phone, String company_logo, String company_qrCode) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.company_logo = company_logo;
        this.company_qrCode = company_qrCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public void setCompany_qrCode(String company_qrCode) {
        this.company_qrCode = company_qrCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public String getCompany_qrCode() {
        return company_qrCode;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }
}
