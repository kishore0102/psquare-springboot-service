package edu.daemondev.psquare;

import java.util.regex.Pattern;

public class Constants {

    public static final String JWT_TOKEN_SECRET_KEY = System.getenv("JWT_TOKEN_SECRET_KEY");
    public static final int JWT_TOKEN_VALIDITY = 2 * 60 * 60 * 1000;

    public static final String OTP_EMAIL = System.getenv("OTP_EMAIL");
    public static final String OTP_PWD = System.getenv("OTP_PWD");
    public static final String MAIL_HOST = System.getenv("MAIL_HOST");
    public static final String SENDER_MAIL_ID = System.getenv("SENDER_MAIL_ID");

    public static final String emailValidationRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final Pattern emailValidationPattern = Pattern.compile(emailValidationRegex);

    public static final String passwordValidationRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final Pattern passwordValidationPattern = Pattern.compile(passwordValidationRegex);

}
