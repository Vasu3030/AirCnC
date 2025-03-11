package com.quest.etna.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.model.Error;
import com.quest.etna.model.Token;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.config.JwtTokenUtil;

@RestController
public class AuthenticationController {

    private static UserRepository userRepository;
    public static JwtTokenUtil jwtTokenUtil;
    static AuthenticationManager authenticationManager;
    private static PasswordEncoder passwordEncoder;

    public AuthenticationController(UserRepository userRepository, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        AuthenticationController.userRepository = userRepository;
        AuthenticationController.authenticationManager = authenticationManager;
        AuthenticationController.passwordEncoder = passwordEncoder;
        AuthenticationController.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            String username = user.getUsername().toLowerCase();
            String password = user.getPassword();

            if (username == null || username.isEmpty()) {
                Error e = new Error("Username is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(e);
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                Error e = new Error("Password is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(e);
            }

            if (userRepository.findByUsername(username).isPresent()) {
                Error e = new Error("Username already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(e);
            }

            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(UserRole.ROLE_USER);
            userRepository.save(user);
            Integer userId = userRepository.findByUsername(username).get().getId();
            UserDetails userDetails = new UserDetails(username, UserRole.ROLE_USER, userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(userDetails);
        } catch (Exception e) {
            Error err = new Error("Username is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(err);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // Generate JWT token
            User userGet = userRepository.findByUsername(username).get();
            JwtUserDetails userDetails = JwtUserDetails.build(userGet);
            String token = jwtTokenUtil.generateToken(userDetails);
            Token resToken = new Token(token);

            return ResponseEntity.ok(resToken);
        } catch (Exception e) {
            Error err = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {

        try {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            System.out.println(token);
            String username = jwtTokenUtil.getUsernameFromToken(token);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDetails userDetails = new UserDetails(user.getUsername(), user.getRole(), user.getId());

            return ResponseEntity.ok(userDetails);
        } catch (Exception e) {
            Error err = new Error("Bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }
}
