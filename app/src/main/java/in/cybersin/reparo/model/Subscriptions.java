package in.cybersin.reparo.model;

public class Subscriptions {
    String Price, Img, High,High1,High2,High3;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getHigh() {
        return High;
    }

    public void setHigh(String high) {
        High = high;
    }

    public String getHigh1() {
        return High1;
    }

    public void setHigh1(String High1) {
        this.High1 = High1;
    }

    public String getHigh2() {
        return High2;
    }

    public void setHigh2(String high2) {
        High2 = high2;
    }

    public String getHigh3() {
        return High3;
    }

    public void setHigh3(String high3) {
        High3 = high3;
    }

    public Subscriptions(String price, String img, String high, String high1, String high2, String high3) {
        Price = price;
        Img = img;
        High = high;
        High1 = high1;
        High2 = high2;
        High3 = high3;
    }

    public Subscriptions() {
    }


}