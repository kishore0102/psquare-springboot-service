package edu.daemondev.psquare;

import java.util.regex.Pattern;

public class Constants {

    public static final String JWT_TOKEN_SECRET_KEY = "PsquareJWTsecretAPIkey";
    public static final int JWT_TOKEN_VALIDITY = 2 * 60 * 60 * 1000;

    public static final String OTP_EMAIL = "71801f0d1cff5c12c9c62cb50b3c3244";
    public static final String OTP_PWD = "9c27668b5c75028c61e1f35fddaf4a01";
    public static final String MAIL_HOST = "in-v3.mailjet.com";

    public static final String emailValidationRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final Pattern emailValidationPattern = Pattern.compile(emailValidationRegex);

    public static final String passwordValidationRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final Pattern passwordValidationPattern = Pattern.compile(passwordValidationRegex);

}
