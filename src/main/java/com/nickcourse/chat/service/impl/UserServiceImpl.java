package com.nickcourse.chat.service.impl;

import com.nickcourse.chat.exception.UserException;
import com.nickcourse.chat.model.User;
import com.nickcourse.chat.repository.UserRepository;
import com.nickcourse.chat.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  public List<User> getAll() {
    log.info("Get All Users");
    return userRepository.findAll();
  }

  public User getById(long id) {
    log.info("Find User By Id");
    return userRepository.findById(id)
        .orElseThrow(() -> new UserException("User with id " + id + " does not exist",
            HttpStatus.NOT_FOUND));
  }

  public User create(User user) {
    log.info("Create User");
    return userRepository.save(user);
  }
}





