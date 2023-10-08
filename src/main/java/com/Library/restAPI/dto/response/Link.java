package com.Library.restAPI.dto.response;

public record Link(Long id, String href) {

    public Link(Long id, String href) {
        this.id = id;
        String apiUrl = "http://localhost:8080/api/v1/";
        this.href = apiUrl + href + id.toString();
    }
}