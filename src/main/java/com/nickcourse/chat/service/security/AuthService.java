package com.nickcourse.chat.service.security;

import com.nickcourse.chat.model.dto.LoginDto;
import com.nickcourse.chat.model.dto.RegisterDto;

public interface AuthService {
  String login(LoginDto loginDto);
  String register(RegisterDto registerDto);
}
