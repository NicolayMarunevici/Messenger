package com.nickcourse.chat.service.impl;

import com.nickcourse.chat.exception.UserException;
import com.nickcourse.chat.model.Role;
import com.nickcourse.chat.model.User;
import com.nickcourse.chat.model.dto.LoginDto;
import com.nickcourse.chat.model.dto.RegisterDto;
import com.nickcourse.chat.repository.RoleRepository;
import com.nickcourse.chat.repository.UserRepository;
import com.nickcourse.chat.security.JwtTokenProvider;
import com.nickcourse.chat.service.AuthService;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                         RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                         JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  // Method, which allows to authenticate user
  @Override
  public String login(LoginDto loginDto) {
    log.info("Attempt to authorize user ");
    Authentication authentication =
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            loginDto.usernameOrEmail(), loginDto.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtTokenProvider.generateToken(authentication);
    log.info("User is authorized");
    return token;
  }

  @Override
  public String register(RegisterDto registerDto) {
    if (userRepository.existsUserByUsername(registerDto.username())) {
      throw new UserException(
          "User with such username already exists", HttpStatus.BAD_REQUEST);
    } else if (userRepository.existsUserByEmail(registerDto.email())) {
      throw new UserException(
          "User with such email already exists", HttpStatus.BAD_REQUEST);
    } else {
      Set<Role> roles = new HashSet<>();
      String userRole = "ROLE_USER";
      roles.add(roleRepository.findByName(userRole).orElseThrow(
          () -> new UserException(
              String.format("Role %s does not exist in database", userRole),
              HttpStatus.BAD_REQUEST)));
      User newUser =
          new User(registerDto.username(), passwordEncoder.encode(registerDto.password()),
              registerDto.email(), roles);
      userRepository.save(newUser);
      return "User has been created";
    }
  }
}
