package talabaty.swever.com.online.Chart.Models;

import java.io.Serializable;
import java.util.List;

public class Bell implements Serializable {
    int Id;
    int IsOffer;
    String Barcode;
    double TotalPrice;
    double ChargeValue;
    String Address;
    String phone;

    String Amount;
    String Price;
    String name, contact_name;
    List<String> SaleType, Sale;

    List<Double> AmountValues, PriceValues;


    public Bell(){

    }

    public void setIsOffer(int isOffer) {
        IsOffer = isOffer;
    }

    public void setAmountValues(List<Double> amountValues) {
        AmountValues = amountValues;
    }

    public void setPriceValues(List<Double> priceValues) {
        PriceValues = priceValues;
    }

    public List<Double> getAmountValues() {
        return AmountValues;
    }

    public List<Double> getPriceValues() {
        return PriceValues;
    }

    public void setSale(List<String> sale) {
        Sale = sale;
    }

    public void setSaleType(List<String> saleType) {
        SaleType = saleType;
    }

    public List<String> getSale() {
        return Sale;
    }

    public List<String> getSaleType() {
        return SaleType;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public void setChargeValue(double chargeValue) {
        ChargeValue = chargeValue;
    }


    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getAddress() {
        return Address;
    }

    public int getId() {
        return Id;
    }

    public String getBarcode() {
        return Barcode;
    }

    public double getChargeValue() {
        return ChargeValue;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrice() {
        return Price;
    }

    public String getAmount() {
        return Amount;
    }

    public String getName() {
        return name;
    }
}
