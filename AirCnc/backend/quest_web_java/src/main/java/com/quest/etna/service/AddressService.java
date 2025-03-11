package com.quest.etna.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.etna.model.Address;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;

@Service
public class AddressService implements IModelService {

    @Autowired
    public AddressRepository addressRepository;

    @Autowired
    public UserRepository userRepository;

    @Override
    public List<Address> getList() {

        Iterable<Address> list = addressRepository.findAll();

        List<Address> addressList = StreamSupport.stream(list.spliterator(), false)
                .collect(Collectors.toList());

        return addressList;

    }

    @Override
    public List<Address> getListNotUser(User user) {

        Iterable<Address> list = addressRepository.findByUserNotOrderByCreationDateDesc(user);

        List<Address> addressList = StreamSupport.stream(list.spliterator(), false)
                .collect(Collectors.toList());

        return addressList;

    }

    @Override
    public List<Address> getListByUser(User user) {

        Iterable<Address> list = addressRepository.findByUserOrderByCreationDateDesc(user);

        List<Address> addressList = StreamSupport.stream(list.spliterator(), false)
                .collect(Collectors.toList());

        return addressList;
    }

    @Override
    public Optional<Address> getOneById(Integer id) {
        Optional<Address> address = addressRepository.findById(id);

        return address;
    }

    @Override
    public Address create(Address address, User user) {

        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public Address update(Address address) {

        return addressRepository.save(address);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Address> address = addressRepository.findById(id);
        if (address.isPresent()) {
            addressRepository.delete(address.get());
            return true;
        } else {
            return false;
        }
    }

}
