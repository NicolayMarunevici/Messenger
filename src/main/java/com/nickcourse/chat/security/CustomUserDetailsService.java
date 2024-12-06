package com.nickcourse.chat.security;

import com.nickcourse.chat.exception.UserException;
import com.nickcourse.chat.model.security.User;
import com.nickcourse.chat.repository.security.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  //  find user by username or email and check authority
  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
        () -> new UserException(
            "User not found with such Username or Email: " + usernameOrEmail,
            HttpStatus.BAD_REQUEST));

    Set<GrantedAuthority> authorities = user
        .getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.getName())).collect(
            Collectors.toSet());

    return new org.springframework.security.core.userdetails.User(user.getEmail(),
        user.getPassword(), authorities);
  }
}
