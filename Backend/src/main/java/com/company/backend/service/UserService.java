package com.company.backend.service;

import com.company.backend.entity.User;

import java.net.ConnectException;

public interface UserService {

    User getUser(String login);

    void save(User user) throws ConnectException;
}
