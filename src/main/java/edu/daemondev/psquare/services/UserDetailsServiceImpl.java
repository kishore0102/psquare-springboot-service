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

    public String HashedPasswordGenerator(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public UserDetails registerUser(String email, String firstname, String lastname, String password)
            throws PsquareAuthException {
        if (userDetailsRepo.getCountByEmail(email) > 0) {
            throw new PsquareAuthException("Email already registered");
        }

        boolean error = false;
        StringBuffer errorData = new StringBuffer();
        Matcher emailMatcher = Constants.emailValidationPattern.matcher(email);
        Matcher passwordMatcher = Constants.passwordValidationPattern.matcher(password);
        if (!emailMatcher.matches()) {
            error = true;
            errorData.append("email ");
        }
        if (!passwordMatcher.matches()) {
            error = true;
            errorData.append("password ");
        }
        if (error) {
            throw new PsquareAuthException("Inputs not following the criteria - " + errorData.toString());
        }

        char status = 'N';
        String passhash = HashedPasswordGenerator(password);
        try {
            userDetailsRepo.addUserDetails(email, firstname, lastname, status, passhash);
        } catch (Exception err) {
            throw new PsquareAuthException("Unexpected error during register, please try again later");
        }
        return getUserByEmail(email);
    }

    public UserDetails getUserByEmail(String email) throws PsquareAuthException {
        try {
            return userDetailsRepo.getUserDetailsByEmail(email);
        } catch (Exception err) {
            throw new PsquareAuthException("Invalid login details");
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

        if (user.getStatus() == 'N') {
            triggerOTPToEmail(user);
            throw new PsquareAuthException("Account is not activated - OTP sent to respective mail");
        }

        if (user.getStatus() == 'L') {
            throw new PsquareAuthException("Account is locked. Please use forgot password option to reset.");
        }

        if (!BCrypt.checkpw(password, user.getPasshash())) {
            incrementUserLock(user);
            if (user.getLockcount() >= 3) {
                user.setStatus('L');
                userDetailsRepo.save(user);
                throw new PsquareAuthException(
                        "Account locked due to multiple wrong entries. Please use forgot password option to reset.");
            }
            throw new PsquareAuthException("Invalid email / Password");
        }

        user.setLockcount(0);
        userDetailsRepo.save(user);
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
            user.setLockcount(user.getLockcount() + 1);
            userDetailsRepo.save(user);
        } catch (Exception err) {
            throw new PsquareAuthException("Unexpected error during login, please try again later.");
        }
    }

    public void triggerOTPToEmail(UserDetails user) {
        int otp = ThreadLocalRandom.current().nextInt(100000, 999999);
        String otpvalue = String.valueOf(otp);
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", Constants.MAIL_HOST);
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Constants.OTP_EMAIL, Constants.OTP_PWD);
                }
            });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("tekbus29@gmail.com", false));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            msg.setSubject("Psquare activation");
            msg.setContent("Psquare activation - valid for 10 mins only", "text/html");
            msg.setText("OTP : " + otpvalue);
            Transport.send(msg);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(11));
            user.setOtp(otpvalue);
            user.setOtpts(currentTimestamp);
            user.setOtpvalidator(0);
            userDetailsRepo.save(user);
        } catch (Exception err) {
            System.out.println("Exception while sending otp - " + err);
            throw new PsquareAuthException("Unexpected error while sending OTP, please try again later.");
        }
    }

    public void validateOTPAndActivate(UserDetails user, String otp) {
        if (user.getOtp() == null || user.getOtpts() == null) {
            throw new PsquareAuthException("OTP failure. Please regenerate OTP.");
        }

        if (otp == null || otp == "") {
            throw new PsquareAuthException("Invalid OTP");
        }

        Timestamp currentTS = new Timestamp(System.currentTimeMillis());
        int comparator = currentTS.compareTo(user.getOtpts());
        if (comparator > 0) {
            user.setOtp(null);
            user.setOtpts(null);
            user.setOtpvalidator(0);
            userDetailsRepo.save(user);
            throw new PsquareAuthException("Expired OTP");
        }

        if (!otp.equals(user.getOtp())) {
            incrementOTPValidator(user);
            if (user.getOtpvalidator() >= 3) {
                user.setOtp(null);
                user.setOtpts(null);
                user.setOtpvalidator(0);
                userDetailsRepo.save(user);
                throw new PsquareAuthException("OTP reset due to multiple wrong entries");
            }
            throw new PsquareAuthException("Incorrect OTP");
        }

        user.setStatus('A');
        user.setOtp(null);
        user.setOtpts(null);
        user.setOtpvalidator(0);
        userDetailsRepo.save(user);
    }

    public void incrementOTPValidator(UserDetails user) throws PsquareAuthException {
        try {
            user.setOtpvalidator(user.getOtpvalidator() + 1);
            userDetailsRepo.save(user);
        } catch (Exception err) {
            throw new PsquareAuthException("Unexpected error during validation, please try again later.");
        }
    }

    public UserDetails changeUserPassword(UserDetails user, String oldpassword, String newpassword) {
        Matcher passwordMatcher = Constants.passwordValidationPattern.matcher(newpassword);
        if (!passwordMatcher.matches()) {
            throw new PsquareAuthException("Inputs not following the criteria - new password ");
        }
        user.setPasshash(HashedPasswordGenerator(newpassword));
        userDetailsRepo.save(user);
        return user;
    }

    public UserDetails resetUserPassword(UserDetails user, String newpassword) {
        Matcher passwordMatcher = Constants.passwordValidationPattern.matcher(newpassword);
        if (!passwordMatcher.matches()) {
            throw new PsquareAuthException("Inputs not following the criteria - new password ");
        }
        user.setPasshash(HashedPasswordGenerator(newpassword));
        userDetailsRepo.save(user);
        return user;
    }

}
