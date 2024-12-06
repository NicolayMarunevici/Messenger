package com.nickcourse.chat.repository.security;


import com.nickcourse.chat.model.security.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  Optional<User> findByUsernameOrEmail(String username, String email);
  Optional<User> findByUsername(String username);
  Boolean existsUserByUsername(String username);
  Boolean existsUserByEmail(String email);
}
