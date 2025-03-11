package com.quest.etna.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quest.etna.model.Address;
import com.quest.etna.model.Booking;
import com.quest.etna.model.User;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.BookingRepository;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Booking> getAllBookings() {

        Iterable<Booking> list = bookingRepository.findAll();

        List<Booking> bookingList = StreamSupport.stream(list.spliterator(), false)
                .collect(Collectors.toList());

        return bookingList;
    }

    @Override
    public List<Booking> getBookingsByUser(User user) {

        Iterable<Booking> list = bookingRepository.findByUserOrderByCreatedAtDesc(user);

        List<Booking> bookingList = StreamSupport.stream(list.spliterator(), false)
                .collect(Collectors.toList());

        return bookingList;
    }

    @Override
    public List<Booking> getBookingsByUserAndStatus(User user, String status) {

        Iterable<Booking> list = bookingRepository.findBookingsByUserIdAndStatus(user.getId(), status);

        List<Booking> bookingList = StreamSupport.stream(list.spliterator(), false)
                .collect(Collectors.toList());

        return bookingList;
    }

    @Override
    public List<Booking> getBookingsByUserAndStatusAcceptedOrStatusProgress(User user) {

        Iterable<Booking> list = bookingRepository.findBookingsByUserIdAndStatusAcceptedOrStatusProgress(user.getId());

        List<Booking> bookingList = StreamSupport.stream(list.spliterator(), false)
                .collect(Collectors.toList());

        return bookingList;
    }

    @Override
    public List<Booking> getBookingsByAddress(Address address) {

        Iterable<Booking> list = bookingRepository.findByAddress(address);

        List<Booking> bookingList = StreamSupport.stream(list.spliterator(), false)
                .collect(Collectors.toList());

        return bookingList;
    }

    @Override
    public Optional<Booking> getBookingById(Integer id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Booking booking) {

        return bookingRepository.save(booking);
    }

    @Override
    public boolean deleteBookingById(Integer id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            bookingRepository.delete(booking.get());
            return true;
        } else {
            return false;
        }
    }

    public void updateBookingStatus() {
        List<Booking> bookings = bookingRepository.findAllByStatusNot("ended");
        System.out.println(bookings);
        for (Booking booking : bookings) {
            if (booking.getToDate().before(new Date())) {
                if (booking.getStatus().equals("progress")) {
                    booking.setStatus("ended");
                    bookingRepository.save(booking);
                }
                if (booking.getStatus().equals("pending")) {
                    bookingRepository.delete(booking);
                }
            }
            if (booking.getFromDate().before(new Date())) {
                if (booking.getStatus().equals("accepted")) {
                    booking.setStatus("progress");
                    bookingRepository.save(booking);
                }
            }
        }
    }

    public List<Booking> getConflictingBookings(Address address, Date fromDate, Date toDate, String status) {
        return bookingRepository.findAllByAddressAndToDateGreaterThanEqualAndFromDateLessThanEqualAndStatusNot(address,
                fromDate, toDate, status);
    }

}
