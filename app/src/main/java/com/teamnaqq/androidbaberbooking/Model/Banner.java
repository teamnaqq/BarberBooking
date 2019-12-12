package com.teamnaqq.androidbaberbooking.Model;

public class Banner {
    // lookbook e banner usam a mesma classe

    private String image;

    public Banner() {
    }

    public Banner(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
