package com.quest.etna.model;

public class PictureDetails {

    private Integer id;
    private Integer addressId;
    private String url;

    public PictureDetails(Picture picture) {
        this.url = picture.getUrl();
        this.id = picture.getId();
        this.addressId = picture.getAddress().getId();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}
