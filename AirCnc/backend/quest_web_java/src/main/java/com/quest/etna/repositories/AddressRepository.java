package com.quest.etna.repositories;

import com.quest.etna.model.Address;
import com.quest.etna.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
    List<Address> findByUser(User user);

    List<Address> findByUserOrderByCreationDateDesc(User user);

    List<Address> findByUserNotOrderByCreationDateDesc(User user);

    List<Address> findByUserNot(User user);

    Optional<Address> findById(Integer id);
}