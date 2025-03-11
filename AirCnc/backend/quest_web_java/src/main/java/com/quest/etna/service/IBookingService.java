package com.quest.etna.service;

import com.quest.etna.model.Address;
import com.quest.etna.model.Booking;
import com.quest.etna.model.User;

import java.util.List;
import java.util.Optional;

public interface IBookingService {

    List<Booking> getAllBookings();

    List<Booking> getBookingsByUser(User user);

    List<Booking> getBookingsByUserAndStatus(User user, String status);

    List<Booking> getBookingsByUserAndStatusAcceptedOrStatusProgress(User user);

    List<Booking> getBookingsByAddress(Address address);

    Optional<Booking> getBookingById(Integer id);

    Booking createBooking(Booking booking);

    Booking updateBooking(Booking booking);

    boolean deleteBookingById(Integer id);
}