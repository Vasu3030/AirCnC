package com.quest.etna.model;

import java.util.Date;
import java.util.List;

public class AddressDetails {
    private Integer id;
    private String name;
    private String street;
    private String postalCode;
    private String city;
    private String country;
    private Integer price;
    private Integer userId;
    private String username;
    private String role;
    private Date creationDate;
    private Date updatedDate;
    private List<PictureDetails> pictureDetails;

    public AddressDetails(Address address, List<PictureDetails> pictureDetails) {
        this.id = address.getId();
        this.name = address.getName();
        this.street = address.getStreet();
        this.postalCode = address.getPostalCode();
        this.city = address.getCity();
        this.country = address.getCountry();
        this.price = address.getPrice();
        this.userId = address.getUser().getId();
        this.username = address.getUser().getUsername();
        this.role = address.getUser().getRole().toString();
        this.creationDate = address.getCreationDate();
        this.updatedDate = address.getUpdatedDate();
        this.pictureDetails = pictureDetails;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<PictureDetails> getPictureDetails() {
        return pictureDetails;
    }

    public void setPictureDetail(List<PictureDetails> pictureDetails) {
        this.pictureDetails = pictureDetails;
    }
}
