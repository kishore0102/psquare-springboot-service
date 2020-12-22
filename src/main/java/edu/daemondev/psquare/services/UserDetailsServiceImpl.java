package edu.daemondev.psquare.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.daemondev.psquare.Constants;
import edu.daemondev.psquare.exceptions.PsquareAuthException;
import edu.daemondev.psquare.models.UserDetails;
import edu.daemondev.psquare.repositories.UserDetailsRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    UserDetailsRepo userDetailsRepo;

    public void registerUser(String email, String firstname, String lastname, String password)
            throws PsquareAuthException {
        if (userDetailsRepo.getCountByEmail(email) > 0) {
            throw new PsquareAuthException("Email already registered!");
        }

        boolean error = false;
        StringBuffer errorData = new StringBuffer();

        Matcher emailMatcher = Constants.emailValidationPattern.matcher(email);
        if (!emailMatcher.matches()) {
            error = true;
            errorData.append("email ");
        }

        Matcher firstnameMatcher = Constants.firstnameValidationPattern.matcher(firstname);
        if (!firstnameMatcher.matches()) {
            error = true;
            errorData.append("firstname ");
        }

        Matcher passwordMatcher = Constants.passwordValidationPattern.matcher(password);
        if (!passwordMatcher.matches()) {
            error = true;
            errorData.append("password ");
        }

        if (error) {
            throw new PsquareAuthException("Invalid inputs - " + errorData.toString());
        }

        char status = 'N';
        String passhash = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            userDetailsRepo.addUserDetails(email, firstname, lastname, status, passhash);
        } catch (Exception err) {
            throw new PsquareAuthException("Unexpected error during register, please try again later");
        }

    }

    public UserDetails validateUser(String email, String password) throws PsquareAuthException {
        UserDetails user;
        try {
            user = userDetailsRepo.getUserDetailsByEmail(email);
        } catch (Exception err) {
            throw new PsquareAuthException("Unexpected error during validation, please try again later");
        }

        if (user == null) {
            throw new PsquareAuthException("Invalid login details");
        }

        if (!BCrypt.checkpw(password, user.getPasshash())) {
            incrementUserLock(user);
            throw new PsquareAuthException("Invalid email / Password");
        }

        if (user.getStatus() == 'N') {
            throw new PsquareAuthException("Account is not activated");
        }

        return user;
    }

    public String generateJWTToken(UserDetails user) throws PsquareAuthException {
        long currentTimestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.JWT_TOKEN_SECRET_KEY)
                .setIssuedAt(new Date(currentTimestamp))
                .setExpiration(new Date(currentTimestamp + Constants.JWT_TOKEN_VALIDITY))
                .claim("userid", user.getUserid()).claim("email", user.getEmail())
                .claim("firstname", user.getFirstname()).claim("lastname", user.getLastname())
                .claim("status", user.getStatus()).compact();
        return token;
    }

    public void incrementUserLock(UserDetails user) throws PsquareAuthException {
        try {
            userDetailsRepo.incrementUserLock(user.getEmail());
        } catch (Exception err) {
            throw new PsquareAuthException("Unexpected error during login, please try again later");
        }
    }

    public void triggerOTPMail(String email) {
        int otp = ThreadLocalRandom.current().nextInt(100000, 999999);
        String otpvalue = String.valueOf(otp);
        System.out.println("otp - " + otpvalue);
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("tekbus29@gmail.com", "kishoresudar");
                }
            });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("tekbus29@gmail.com", false));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            msg.setSubject("Psquare activation");
            msg.setContent("Psquare activation", "text/html");
            msg.setText("OTP : " + otpvalue);
            Transport.send(msg);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
            userDetailsRepo.updateOTPByEmail(email, otpvalue, currentTimestamp);
        } catch (Exception err) {
            System.out.println("Unexpected error while sending OTP, please try again later \n " + err);
            throw new PsquareAuthException("Unexpected error while sending OTP, please try again later");
        }
    }

    public void validateOTP(UserDetails user, String otp) {
        Timestamp currentTS = new Timestamp(System.currentTimeMillis());
        int comparator = currentTS.compareTo(user.getOtpts());
        if (comparator > 0 || !otp.equals(user.getOtp())) {
            throw new PsquareAuthException("Invalid / Expired OTP");
        }
        userDetailsRepo.activateUserOTPBased(user.getEmail(), 'A');
    }

}
