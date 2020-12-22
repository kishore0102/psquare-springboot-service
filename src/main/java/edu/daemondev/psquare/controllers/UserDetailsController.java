package edu.daemondev.psquare.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.daemondev.psquare.models.UserDetails;
import edu.daemondev.psquare.repositories.UserDetailsRepo;
import edu.daemondev.psquare.services.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/user/")
public class UserDetailsController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserDetailsRepo userDetailsRepo;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        UserDetails validatedUser = userDetailsService.validateUser(email, password);
        String token = userDetailsService.generateJWTToken(validatedUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String firstname = (String) request.get("firstname");
        String lastname = (String) request.get("lastname");
        String password = (String) request.get("password");
        userDetailsService.registerUser(email, firstname, lastname, password);
        UserDetails registeredUser = userDetailsService.validateUser(email, password);
        String token = userDetailsService.generateJWTToken(registeredUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        UserDetails validatedUser = userDetailsRepo.getUserDetailsByEmail(email);
        userDetailsService.triggerOTPMail(validatedUser.getEmail());
        String token = userDetailsService.generateJWTToken(validatedUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/validateOTP")
    public ResponseEntity<?> validateUserOTP(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        String otp = (String) request.get("otp");
        UserDetails validatedUser = userDetailsRepo.getUserDetailsByEmail(email);
        userDetailsService.validateOTP(validatedUser, otp);
        String token = userDetailsService.generateJWTToken(validatedUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseEntity.ok(map);
    }

}
