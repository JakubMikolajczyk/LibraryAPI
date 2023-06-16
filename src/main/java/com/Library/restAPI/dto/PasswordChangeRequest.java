package com.Library.restAPI.dto;

public record PasswordChangeRequest (
        String oldPassword,
        String newPassword
){
}
