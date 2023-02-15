package com.example.androidserver_asm.Models;

import java.io.Serializable;

public class Products implements Serializable {
    private Integer id;
    private String name;
    private Float price;
    private Integer quantity;
    private String image;
    private Integer category_id;
    private String describes;



    public Products(){

    }

    public Products(String name, Float price, Integer quantity, String image, Integer category_id, String describes) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.category_id = category_id;
        this.describes = describes;
    }

    public Products(Integer id, String name, Float price, Integer quantity, String image, Integer category_id, String describes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.category_id = category_id;
        this.describes = describes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }
}
