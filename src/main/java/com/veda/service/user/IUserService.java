package com.veda.service.user;

import com.veda.exception.UserNotFoundException;
import com.veda.model.User;

public interface IUserService {
    public User findUser(String id, String password) throws UserNotFoundException;
    public void registerUser(User user);
}
