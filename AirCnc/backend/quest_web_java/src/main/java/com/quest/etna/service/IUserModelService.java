package com.quest.etna.service;

import java.util.List;
import java.util.Optional;

import com.quest.etna.model.Address;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;

public interface IUserModelService<T> {

    public List<User> getList();

    public Optional<User> getOneById(Integer id);

    public User update(User user);

    public Boolean delete(Integer id);
}