package com.quest.etna.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.Booking;
import com.quest.etna.model.User;
import com.quest.etna.model.Address;
import com.quest.etna.model.BookingDetails;
import com.quest.etna.model.Error;
import com.quest.etna.model.Token;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.service.AddressService;
import com.quest.etna.service.BookingService;
import com.quest.etna.config.JwtTokenUtil;

import java.text.SimpleDateFormat;

@EnableScheduling
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BookingService bookingService;

    @Scheduled(cron = "0 0 * * * *") // chaque heure
    // @Scheduled(cron = "*/1 * * * * *") // chaque seconde
    public void updateBookingStatus() {
        bookingService.updateBookingStatus();
    }

    @PostMapping("")
    public ResponseEntity<?> createBooking(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to,
            @RequestParam(name = "price") Integer price,
            @RequestParam(name = "id_address") Integer addressId, Principal principal) {

        try {
            JwtUserDetails user = getUserFromPrincipal(principal);
            String username = user.getUsername();
            Optional<User> userGet = userRepository.findByUsername(username);
            if (!userGet.isPresent()) {
                Error err = new Error("User Not found");
                return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
            }

            Optional<Address> address = addressService.getOneById(addressId);
            if (!address.isPresent()) {
                Error err = new Error("Address Not found");
                return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
            }
            if (address.get().getUser().equals(userGet.get())) {
                Error err = new Error("Can't book this address");
                return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
            }

            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(from);
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

            // Check date
            if (toDate.equals(fromDate) || toDate.before(fromDate) || toDate.before(new Date())
                    || fromDate.before(new Date())) {
                Error err = new Error("Date values invalid");
                return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
            }

            List<Booking> conflictingBookings = bookingService.getConflictingBookings(address.get(), fromDate, toDate,
                    "ended");

            if (!conflictingBookings.isEmpty()) {
                Error err = new Error("Address used to those dates");
                return new ResponseEntity<>(err, HttpStatus.CONFLICT);
            }

            Booking booking = new Booking();
            booking.setFromDate(fromDate);
            booking.setToDate(toDate);
            booking.setPrice(price);
            booking.setUser(userGet.get());
            booking.setAddress(address.get());

            Booking savedBooking = bookingService.createBooking(booking);
            BookingDetails bookingDetails = new BookingDetails(savedBooking);
            return new ResponseEntity<>(bookingDetails, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getBookingByUserId(@PathVariable("id") Integer id, Principal principal) {

        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (userGet.getId().equals(id) == false && userGet.getRole().toString() == "ROLE_USER") {

            Error err = new Error("Not your bookings");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }
        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        Optional<User> userCheck = userRepository.findById(id);
        if (userCheck.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        List<Booking> bookings = bookingService.getBookingsByUser(userCheck.get());

        for (Booking booking : bookings) {
            BookingDetails bookingDetails = new BookingDetails(booking);
            bookingDetailsList.add(bookingDetails);
        }

        return new ResponseEntity<>(bookingDetailsList, HttpStatus.OK);
    }

    @GetMapping("/user/status/{id}")
    public ResponseEntity<?> getBookingByUserIdAndStatus(@PathVariable("id") Integer id,
            @RequestParam("status") String status,
            Principal principal) {
        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (!userGet.getId().equals(id) && userGet.getRole().toString().equals("ROLE_USER")) {
            Error err = new Error("Not your bookings");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }

        Optional<User> userCheck = userRepository.findById(id);
        if (!userCheck.isPresent()) {
            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        List<Booking> bookings = bookingService.getBookingsByUserAndStatus(userCheck.get(), status);
        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingDetails bookingDetails = new BookingDetails(booking);
            bookingDetailsList.add(bookingDetails);
        }

        return new ResponseEntity<>(bookingDetailsList, HttpStatus.OK);
    }

    @GetMapping("/user/incoming/{id}")
    public ResponseEntity<?> getBookingByUserIdAndStatusAcceptedOrProgress(@PathVariable("id") Integer id,
            Principal principal) {
        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (!userGet.getId().equals(id) && userGet.getRole().toString().equals("ROLE_USER")) {
            Error err = new Error("Not your bookings");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }

        Optional<User> userCheck = userRepository.findById(id);
        if (!userCheck.isPresent()) {
            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        List<Booking> bookings = bookingService.getBookingsByUserAndStatusAcceptedOrStatusProgress(userCheck.get());
        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingDetails bookingDetails = new BookingDetails(booking);
            bookingDetailsList.add(bookingDetails);
        }

        return new ResponseEntity<>(bookingDetailsList, HttpStatus.OK);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<?> getBookingByAddressId(@PathVariable("id") Integer id, Principal principal) {

        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        List<BookingDetails> bookingDetailsList = new ArrayList<>();

        Optional<Address> addressCheck = addressRepository.findById(id);
        if (addressCheck.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        if (addressCheck.get().getUser().getId().equals(userGet.getId())
                || userGet.getRole().toString() == "ROLE_ADMIN") {
            List<Booking> bookings = bookingService.getBookingsByAddress(addressCheck.get());

            for (Booking booking : bookings) {
                BookingDetails bookingDetails = new BookingDetails(booking);
                bookingDetailsList.add(bookingDetails);
            }

            return new ResponseEntity<>(bookingDetailsList, HttpStatus.OK);
        } else {
            Error err = new Error("Not your address");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable("id") Integer id, Principal principal) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (userGet.getRole().toString() == "ROLE_USER" && userGet != booking.get().getAddress().getUser()
                && userGet != booking.get().getUser()) {
            Error err = new Error("Forbidden");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }

        Boolean deleted = bookingService.deleteBookingById(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", deleted);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable("id") Integer id,
            @RequestParam(name = "status") Boolean status,
            Principal principal) {
        Optional<Booking> booking = bookingService.getBookingById(id);

        if (booking.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (userGet.getRole().toString() == "ROLE_USER" && userGet != booking.get().getAddress().getUser()) {
            Error err = new Error("Forbidden");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }
        if (status.equals(true)) {
            booking.get().setStatus("accepted");
            Booking updated = bookingService.updateBooking(booking.get());
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            Boolean deleted = bookingService.deleteBookingById(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("success", deleted);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    private JwtUserDetails getUserFromPrincipal(Principal principal) {
        Authentication authentication = (Authentication) principal;
        return (JwtUserDetails) authentication.getPrincipal();
    }
}
