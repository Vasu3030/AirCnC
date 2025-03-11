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

import com.quest.etna.model.Comment;
import com.quest.etna.model.CommentDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.Address;
import com.quest.etna.model.BookingDetails;
import com.quest.etna.model.Error;
import com.quest.etna.model.Token;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.service.AddressService;
import com.quest.etna.service.BookingService;
import com.quest.etna.config.JwtTokenUtil;

import java.text.SimpleDateFormat;

@EnableScheduling
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("")
    public ResponseEntity<?> createdComment(
            @RequestParam(name = "content") String content,
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

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setUser(userGet.get());
            comment.setAddress(address.get());

            Comment savedComment = commentRepository.save(comment);
            CommentDetails commentDetails = new CommentDetails(savedComment);
            return new ResponseEntity<>(commentDetails, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Integer id, Principal principal) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        JwtUserDetails user = getUserFromPrincipal(principal);
        String username = user.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (userGet.getRole().toString() == "ROLE_USER" && userGet != comment.get().getAddress().getUser()
                && userGet != comment.get().getUser()) {
            Error err = new Error("Forbidden");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }

        commentRepository.delete(comment.get());
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private JwtUserDetails getUserFromPrincipal(Principal principal) {
        Authentication authentication = (Authentication) principal;
        return (JwtUserDetails) authentication.getPrincipal();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentByAddressId(@PathVariable("id") Integer id) {
        Optional<Address> address = addressService.getOneById(id);
        if (address.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        List<Comment> comments = commentRepository.findByAddressOrderByCreatedAtDesc(address.get());
        List<CommentDetails> commentDetailsList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDetails commentDetails = new CommentDetails(comment);
            commentDetailsList.add(commentDetails);
        }

        return new ResponseEntity<>(commentDetailsList, HttpStatus.OK);
    }
}
