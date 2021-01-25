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
import edu.daemondev.psquare.services.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/user/")
public class UserDetailsController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String password = (String) request.get("password");
        UserDetails validatedUser = userDetailsService.validateUser(email, password);
        String token = userDetailsService.generateJWTToken(validatedUser);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        Map<String, Object> user = new HashMap<>();
        user.put("email", validatedUser.getEmail());
        user.put("firtsname", validatedUser.getFirstname());
        user.put("lastname", validatedUser.getLastname());
        user.put("status", validatedUser.getStatus());
        map.put("user", user);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String firstname = (String) request.get("firstname");
        String lastname = (String) request.get("lastname");
        String password = (String) request.get("password");
        userDetailsService.registerUser(email, firstname, lastname, password);
        return sendOTP(request);
    }

    @PostMapping("/sendOTP")
    public ResponseEntity<?> sendOTP(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        UserDetails user = userDetailsService.getUserByEmail(email);
        userDetailsService.triggerOTPToEmail(user);
        Map<String, String> map = new HashMap<>();
        map.put("message", "OTP sent to " + email);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/registerOTP")
    public ResponseEntity<?> registerOTP(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String otp = (String) request.get("otp");
        UserDetails user = userDetailsService.getUserByEmail(email);
        userDetailsService.validateOTPAndActivate(user, otp);
        Map<String, String> map = new HashMap<>();
        map.put("message", "registered successfully");
        return ResponseEntity.ok(map);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String oldpassword = (String) request.get("oldpassword");
        String newpassword = (String) request.get("newpassword");
        UserDetails user = userDetailsService.getUserByEmail(email);
        user = userDetailsService.changeUserPassword(user, oldpassword, newpassword);
        String token = userDetailsService.generateJWTToken(user);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, Object> request) {
        return sendOTP(request);
    }

    @PostMapping("/forgotPasswordOTP")
    public ResponseEntity<?> forgotPasswordOTP(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String otp = (String) request.get("otp");
        String newpassword = (String) request.get("newpassword");
        UserDetails user = userDetailsService.getUserByEmail(email);
        userDetailsService.validateOTPAndActivate(user, otp);
        user = userDetailsService.resetUserPassword(user, newpassword);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Password reset success. Please try login using new password.");
        return ResponseEntity.ok(map);
    }

}
