package com.example.project;

public class UserToken {
    private int menuNumber;
    private int menuPrice;
    private int menuTotalPrice;
    private String payMethod;

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public int getMenuTotalPrice() {
        return menuTotalPrice;
    }

    public void setMenuTotalPrice(int menuTotalPrice) {
        this.menuTotalPrice = menuTotalPrice;
    }

    public int getMenuNumber() {
        return menuNumber;
    }

    public void setMenuNumber(int menuNumber) {
        this.menuNumber = menuNumber;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }
}
