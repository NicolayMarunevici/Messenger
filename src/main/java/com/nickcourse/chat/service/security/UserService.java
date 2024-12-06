package com.nickcourse.chat.service.security;

import com.nickcourse.chat.model.security.User;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  List<User> getAll();

  User getById(long id);

  User create(User user);
}
