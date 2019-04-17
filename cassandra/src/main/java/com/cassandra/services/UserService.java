package com.cassandra.services;

import com.cassandra.dao.UserDao;
import com.cassandra.models.User;

public class UserService {

    private UserDao usersDao = new UserDao();

    public UserService() {
    }

    public Double showBalance(String number) {
        return usersDao.findByNumber(number);
    }

    public void deleteUser(User user) {
        usersDao.delete(user);
    }

    public void updateUser(User user) {
        usersDao.update(user);
    }

}