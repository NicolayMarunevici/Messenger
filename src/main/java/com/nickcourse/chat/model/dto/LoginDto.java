package com.nickcourse.chat.model.dto;

public record LoginDto(
     String usernameOrEmail,
     String password
) {
}
