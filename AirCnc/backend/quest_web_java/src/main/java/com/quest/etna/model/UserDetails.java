package com.quest.etna.model;

public class UserDetails {
    private String username;
    private UserRole role;
    private Integer id;

    public UserDetails(String username, UserRole role, Integer id) {
        this.username = username;
        this.role = role;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setUsername(Integer id) {
        this.id = id;
    }

}
