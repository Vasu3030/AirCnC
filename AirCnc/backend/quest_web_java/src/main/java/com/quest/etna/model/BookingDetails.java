package com.quest.etna.model;

import java.util.Date;

public class BookingDetails {
    private Integer id;
    private String name;
    private String status;
    private Integer price;
    private Date from;
    private Date to;
    private Integer userId;
    private Integer ownerId;
    private String ownerName;
    private String userName;
    private Integer addressId;
    private Date createdAt;
    private Date updatedAt;

    public BookingDetails(Booking booking) {
        this.id = booking.getId();
        this.name = booking.getAddress().getName();
        this.status = booking.getStatus();
        this.ownerName = booking.getAddress().getUser().getUsername();
        this.userName = booking.getUser().getUsername();
        this.price = booking.getPrice();
        this.from = booking.getFromDate();
        this.to = booking.getToDate();
        this.userId = booking.getUser().getId();
        this.ownerId = booking.getAddress().getUser().getId();
        this.addressId = booking.getAddress().getId();
        this.createdAt = booking.getCreatedAt();
        this.updatedAt = booking.getUpdatedAt();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Date getCreateDate() {
        return createdAt;
    }

    public void setcreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
