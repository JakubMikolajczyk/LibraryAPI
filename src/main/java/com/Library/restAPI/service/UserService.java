package com.Library.restAPI.service;

import com.Library.restAPI.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public User getUserById(Long id);

    public List<User> getAllUsers();

    public List<User> getAllUsersWithoutDeleted();
    public void updateUser(User user);

    public void deleteUserById(Long id);

}
