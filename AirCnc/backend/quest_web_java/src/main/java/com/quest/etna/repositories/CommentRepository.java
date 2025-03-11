package com.quest.etna.repositories;

import java.util.Date;
import java.util.List;

import com.quest.etna.model.Comment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.quest.etna.model.User;
import com.quest.etna.model.Address;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findByUserOrderByCreatedAtDesc(User user);

    List<Comment> findByAddressOrderByCreatedAtDesc(Address address);

}
