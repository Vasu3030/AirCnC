package com.quest.etna.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.quest.etna.model.Picture;

@Repository
public interface PictureRepository extends CrudRepository<Picture, Integer> {

    List<Picture> findByAddressId(Integer addressId);
}
