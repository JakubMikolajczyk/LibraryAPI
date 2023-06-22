package com.Library.restAPI.security;

public class UsernameAndIdPrincipal {

    private String username;
    private Long userId;

    public UsernameAndIdPrincipal(String username, Long userId){
        this.username = username;
        this.userId = userId;
    }

    public String getUsername(){
        return username;
    }

    public Long getUserId(){
        return userId;
    }
}
