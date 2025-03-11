package com.quest.etna.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.User;
import com.quest.etna.model.Picture;
import com.quest.etna.model.Comment;
import com.quest.etna.model.PictureDetails;
import com.quest.etna.model.Address;
import com.quest.etna.model.Booking;
import com.quest.etna.model.AddressDetails;
import com.quest.etna.model.Error;
import com.quest.etna.model.Token;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.BookingRepository;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.PictureRepository;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.service.AddressService;
import com.quest.etna.service.BookingService;
import com.quest.etna.config.JwtTokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("")
    public ResponseEntity<?> getAllAddresses(Principal principal) {

        List<AddressDetails> addressDetailsList = new ArrayList<>();
        JwtUserDetails user = null;
        String username = null;
        Optional<User> userOptional = Optional.empty();

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
            user = (JwtUserDetails) authenticationToken.getPrincipal();
            username = user.getUsername();
            userOptional = userRepository.findByUsername(username);
        }

        if (userOptional.isPresent()) {
            List<Address> addresses = addressService.getListNotUser(userOptional.get());
            for (Address address : addresses) {
                List<Picture> pictures = pictureRepository.findByAddressId(address.getId());
                List<PictureDetails> pictureDetailsList = new ArrayList<>();
                for (Picture picture : pictures) {
                    PictureDetails pictureDetails = new PictureDetails(picture);
                    pictureDetailsList.add(pictureDetails);
                }
                AddressDetails addressDetails = new AddressDetails(address, pictureDetailsList);
                addressDetailsList.add(addressDetails);
            }
            return new ResponseEntity<>(addressDetailsList, HttpStatus.OK);
        } else {
            List<Address> addresses = addressService.getList();
            for (Address address : addresses) {
                List<Picture> pictures = pictureRepository.findByAddressId(address.getId());
                List<PictureDetails> pictureDetailsList = new ArrayList<>();
                for (Picture picture : pictures) {
                    PictureDetails pictureDetails = new PictureDetails(picture);
                    pictureDetailsList.add(pictureDetails);
                }
                AddressDetails addressDetails = new AddressDetails(address, pictureDetailsList);
                addressDetailsList.add(addressDetails);
            }
            return new ResponseEntity<>(addressDetailsList, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable("id") Integer id) {
        Optional<Address> address = addressService.getOneById(id);
        if (address.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        List<Picture> pictures = pictureRepository.findByAddressId(address.get().getId());
        List<PictureDetails> pictureDetailsList = new ArrayList<>();
        for (Picture picture : pictures) {
            PictureDetails pictureDetails = new PictureDetails(picture);
            pictureDetailsList.add(pictureDetails);
        }

        AddressDetails addressDetails = new AddressDetails(address.get(), pictureDetailsList);
        return new ResponseEntity<>(addressDetails, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAddressByUserId(@PathVariable("id") Integer id) {
        List<AddressDetails> addressDetailsList = new ArrayList<>();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
        List<Address> addresses = addressService.getListByUser(user.get());

        for (Address address : addresses) {
            List<Picture> pictures = pictureRepository.findByAddressId(address.getId());
            List<PictureDetails> pictureDetailsList = new ArrayList<>();
            for (Picture picture : pictures) {
                PictureDetails pictureDetails = new PictureDetails(picture);
                pictureDetailsList.add(pictureDetails);
            }
            AddressDetails addressDetails = new AddressDetails(address, pictureDetailsList);
            addressDetailsList.add(addressDetails);
        }

        return new ResponseEntity<>(addressDetailsList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createAddress(@RequestBody Address address, Principal principal) {

        if (address.getName() == null || address.getName().isEmpty()) {
            Error err = new Error("Name can't be null");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        if (address.getStreet() == null || address.getStreet().isEmpty()) {
            Error err = new Error("Street can't be null");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        if (address.getCountry() == null || address.getCountry().isEmpty()) {
            Error err = new Error("Country can't be null");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        if (address.getCity() == null || address.getCity().isEmpty()) {
            Error err = new Error("City can't be null");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        if (address.getPostalCode() == null || address.getPostalCode().isEmpty()) {
            Error err = new Error("Postal Code can't be null");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        if (address.getPrice() == null || address.getPrice().toString().isEmpty()) {
            Error err = new Error("Price can't be null");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        JwtUserDetails user = getUserFromPrincipal(principal);

        if (user == null) {
            Error err = new Error("user can't be null");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        Address createdAddress = addressService.create(address, userGet);
        List<Picture> pictures = pictureRepository.findByAddressId(address.getId());
        List<PictureDetails> pictureDetailsList = new ArrayList<>();
        for (Picture picture : pictures) {
            PictureDetails pictureDetails = new PictureDetails(picture);
            pictureDetailsList.add(pictureDetails);
        }
        AddressDetails addressDetails = new AddressDetails(createdAddress, pictureDetailsList);
        return new ResponseEntity<>(addressDetails, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") Integer id, @RequestBody Address address,
            Principal principal) {

        Optional<Address> addressOptional = addressService.getOneById(id);
        if (addressOptional.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (userGet.getRole().toString() == "ROLE_USER" && userGet != addressOptional.get().getUser()) {
            Error err = new Error("Unauthorized");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }

        Address updatedAddress = addressOptional.get();

        if (address.getStreet() != null && address.getStreet().isEmpty() != true) {
            updatedAddress.setStreet(address.getStreet());
        }
        if (address.getPostalCode() != null && address.getPostalCode().isEmpty() != true) {
            updatedAddress.setPostalCode(address.getPostalCode());
        }
        if (address.getCity() != null && address.getCity().isEmpty() != true) {
            updatedAddress.setCity(address.getCity());
        }
        if (address.getCountry() != null && address.getCountry().isEmpty() != true) {
            updatedAddress.setCountry(address.getCountry());
        }
        if (address.getName() != null && address.getName().isEmpty() != true) {
            updatedAddress.setName(address.getName());
        }
        if (address.getPrice() != null && address.getPrice() >= 0) {
            updatedAddress.setPrice(address.getPrice());
        }
        updatedAddress = addressService.update(updatedAddress);

        List<Picture> pictures = pictureRepository.findByAddressId(address.getId());
        List<PictureDetails> pictureDetailsList = new ArrayList<>();
        for (Picture picture : pictures) {
            PictureDetails pictureDetails = new PictureDetails(picture);
            pictureDetailsList.add(pictureDetails);
        }
        AddressDetails addressDetails = new AddressDetails(updatedAddress, pictureDetailsList);
        return new ResponseEntity<>(addressDetails, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") Integer id, Principal principal) {
        Optional<Address> address = addressService.getOneById(id);
        if (address.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (userGet.getRole().toString() == "ROLE_USER" && userGet != address.get().getUser()) {
            Error err = new Error("Forbidden");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }

        List<Picture> pictures = pictureRepository.findByAddressId(address.get().getId());

        for (Picture picture : pictures) {
            pictureRepository.delete(picture);
        }

        List<Comment> comments = commentRepository.findByAddressOrderByCreatedAtDesc(address.get());

        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }

        List<Booking> bookings = bookingService.getBookingsByAddress(address.get());

        for (Booking booking : bookings) {
            bookingRepository.delete(booking);
        }

        Boolean deleted = addressService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", deleted);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/picture/{id}")
    public ResponseEntity<?> addPictureToAddress(@PathVariable("id") Integer addressId,
            @RequestBody Picture picture, Principal principal) {

        Optional<Address> optionalAddress = addressService.getOneById(addressId);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            JwtUserDetails user = getUserFromPrincipal(principal);
            String username = user.getUsername();
            User userGet = userRepository.findByUsername(username).get();

            if (!userGet.getId().equals(address.getUser().getId())
                    && userGet.getRole().toString().equals("ROLE_USER")) {
                Error err = new Error("Forbidden not your address");
                return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
            }

            picture.setAddress(address);
            pictureRepository.save(picture);
            PictureDetails pictureDetails = new PictureDetails(picture);
            return new ResponseEntity<>(pictureDetails, HttpStatus.CREATED);
        } else {
            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/picture/{id}")
    public ResponseEntity<?> deletePicture(@PathVariable("id") Integer id, Principal principal) {
        Optional<Picture> picture = pictureRepository.findById(id);
        if (picture.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (userGet.getRole().toString() == "ROLE_USER" && userGet != picture.get().getAddress().getUser()) {
            Error err = new Error("Forbidden");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }

        pictureRepository.delete(picture.get());
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private JwtUserDetails getUserFromPrincipal(Principal principal) {
        Authentication authentication = (Authentication) principal;
        return (JwtUserDetails) authentication.getPrincipal();
    }
}
