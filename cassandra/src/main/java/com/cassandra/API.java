package com.cassandra;

import com.cassandra.models.User;
import com.cassandra.services.UserService;

public interface API {
    UserService userService = new UserService();
    User user = new User();
}
