package com.nickcourse.chat.model.dto;


public record RegisterDto(
    String name,
    String username,
    String email,
    String password
) {
}
