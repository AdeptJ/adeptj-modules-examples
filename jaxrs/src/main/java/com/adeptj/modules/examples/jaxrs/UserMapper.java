package com.adeptj.modules.examples.jaxrs;

import com.adeptj.modules.examples.jpa.entity.User;

import java.util.List;

public interface UserMapper {

    User getUser(long id);

    List<User> getUsers();

    void insertUser(User user);
}
