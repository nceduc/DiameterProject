package com.cassandra;


import com.cassandra.dao.UserDao;
import com.cassandra.models.User;

public class Example implements API {
    public static void main(String[] args) {
        user.setNumber("+79818031948");
        user.setBalance(300);
        userService.updateUser(user);
        User user2 = new User("+79191434837", 300);
        userService.updateUser(user2);
        try {
            System.out.println("Баланс пользователя: " + user.getNumber()+" : "+ userService.showBalance(user.getNumber()));
            user.setBalance(500);
            userService.updateUser(user);
            System.out.println("Баланс пользователя "+user.getNumber()+" : " + userService.showBalance(user.getNumber()));
            System.out.println("Баланс пользователя "+user2.getNumber()+" : " + userService.showBalance(user2.getNumber()));
            userService.deleteUser(user);
        }
        catch (NullPointerException e){
            System.out.println("Не найден такой пользователь");
        }

        UserDao.closeConnection();
    }
}
