package com.quest.etna.service;

import java.util.List;
import java.util.Optional;

import com.quest.etna.model.Address;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;

public interface IModelService<T> {

    public List<Address> getList();

    public List<Address> getListNotUser(User user);

    public List<Address> getListByUser(User user);

    public Optional<Address> getOneById(Integer id);

    public Address create(Address entity, User user);

    public Address update(Address address);

    public Boolean delete(Integer id);
}