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
import com.quest.etna.model.Comment;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.UserRepository;

@Service
public class UserService implements IUserModelService {

    @Autowired
    public AddressRepository addressRepository;

    @Autowired
    public CommentRepository commentRepository;

    @Autowired
    public UserRepository userRepository;

    @Override
    public List<User> getList() {

        Iterable<User> userIterable = userRepository.findAll();
        List<User> userList = StreamSupport.stream(userIterable.spliterator(), false)
                .collect(Collectors.toList());
        return userList;

    }

    @Override
    public Optional<User> getOneById(Integer id) {
        Optional<User> user = userRepository.findById(id);

        return user;
    }

    @Override
    public User update(User user) {

        return userRepository.save(user);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            List<Comment> userComments = commentRepository.findByUserOrderByCreatedAtDesc(user.get());
            commentRepository.deleteAll(userComments);
            List<Address> userAddresses = addressRepository.findByUser(user.get());
            addressRepository.deleteAll(userAddresses);
            userRepository.delete(user.get());
            return true;
        } else {
            return false;
        }
    }
}