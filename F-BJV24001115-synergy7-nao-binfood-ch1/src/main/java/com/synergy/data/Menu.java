package com.synergy.data;

import java.util.UUID;

public class Menu {
    private int id;
    private String name;
    private int price;

    public Menu(int id, String name, int price) {
        if (price < 0) {
            price = 0;
        }

        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        if (price < 0) {
            price = 0;
        }

        this.price = price;
    }
}
