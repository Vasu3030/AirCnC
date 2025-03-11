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
import org.springframework.security.authentication.AuthenticationManager;

import com.quest.etna.model.User;
import com.quest.etna.model.Address;
import com.quest.etna.model.UserDetails;
import com.quest.etna.model.Error;
import com.quest.etna.model.Token;
import com.quest.etna.model.UpdatedUserDetails;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.service.AddressService;
import com.quest.etna.service.UserService;
import com.quest.etna.config.JwtTokenUtil;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    static AuthenticationManager authenticationManager;

    public static JwtTokenUtil jwtTokenUtil;

    public UserController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        UserController.authenticationManager = authenticationManager;
        UserController.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers(Principal principal) {

        List<User> users = userService.getList();

        List<UserDetails> userDetailsList = new ArrayList<>();

        for (User user : users) {
            UserDetails userDetails = new UserDetails(user.getUsername(), user.getRole(), user.getId());
            userDetailsList.add(userDetails);
        }

        return new ResponseEntity<>(userDetailsList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Integer id) {
        Optional<User> user = userService.getOneById(id);

        if (!user.isPresent()) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        UserDetails userDetails = new UserDetails(user.get().getUsername(), user.get().getRole(), user.get().getId());

        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") Integer id, @RequestBody User user,
            Principal principal) {

        if (user.getUsername() != null && user.getUsername().isEmpty()) {
            Error err = new Error("Username can't be null");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOptional = userService.getOneById(id);
        if (userOptional.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        JwtUserDetails userPermission = getUserFromPrincipal(principal);
        String username = userPermission.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (userGet.getRole().toString().equals("ROLE_USER") && !id.equals(userGet.getId())) {
            Error err = new Error("Unauthorized, can't change other user YOUNESSSSS");
            return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
        }

        User updatedUser = userOptional.get();

        if (user.getUsername() != null && user.getUsername().isEmpty() != true) {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                Error err = new Error("Username already exist");
                return new ResponseEntity<>(err, HttpStatus.CONFLICT);
            }
            updatedUser.setUsername(user.getUsername());
        }
        if (user.getRole() != null && userGet.getRole().toString() == "ROLE_ADMIN") {
            updatedUser.setRole(user.getRole());
        }

        updatedUser = userService.update(updatedUser);
        UserDetails userDetails = new UserDetails(updatedUser.getUsername(), updatedUser.getRole(),
                updatedUser.getId());

        // Generate JWT token
        User userGetUpdated = userRepository.findByUsername(userDetails.getUsername()).get();
        JwtUserDetails userDetailsUpdated = JwtUserDetails.build(userGetUpdated);
        String token = jwtTokenUtil.generateToken(userDetailsUpdated);
        UpdatedUserDetails updatedUserDetails = new UpdatedUserDetails(token, userDetails);
        return new ResponseEntity<>(updatedUserDetails, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id, Principal principal) {
        Optional<User> user = userService.getOneById(id);
        if (user.isPresent() == false) {

            Error err = new Error("Not found");
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }

        JwtUserDetails userPermission = getUserFromPrincipal(principal);
        String username = userPermission.getUsername();
        User userGet = userRepository.findByUsername(username).get();

        if (userGet.getRole().toString().equals("ROLE_USER") && !id.equals(userGet.getId())) {
            Error err = new Error("Forbidden");
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }

        Boolean deleted = userService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", deleted);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private JwtUserDetails getUserFromPrincipal(Principal principal) {
        Authentication authentication = (Authentication) principal;
        return (JwtUserDetails) authentication.getPrincipal();
    }
}
