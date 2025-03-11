package com.quest.etna.repositories;

import java.util.Date;
import java.util.List;

import com.quest.etna.model.Booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.quest.etna.model.User;
import com.quest.etna.model.Address;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {
    List<Booking> findByUser(User user);

    List<Booking> findByUserOrderByCreatedAtDesc(User user);

    @Query(value = "SELECT * FROM booking " +
            "INNER JOIN address ON address.id = booking.id_address " +
            "INNER JOIN user ON user.id = address.user_id " +
            "WHERE user.id = ?1 AND (booking.status = ?2)", nativeQuery = true)
    List<Booking> findBookingsByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);

    @Query(value = "SELECT * FROM booking " +
            "INNER JOIN address ON address.id = booking.id_address " +
            "INNER JOIN user ON user.id = address.user_id " +
            "WHERE user.id = ?1 AND (booking.status = 'accepted' or booking.status = 'progress')", nativeQuery = true)
    List<Booking> findBookingsByUserIdAndStatusAcceptedOrStatusProgress(@Param("userId") Integer userId);

    List<Booking> findByAddress(Address address);

    List<Booking> findAllByStatusNot(String status);

    List<Booking> findAllByAddressAndToDateGreaterThanEqualAndFromDateLessThanEqualAndStatusNot(Address address,
            Date fromDate, Date toDate, String string);

}
