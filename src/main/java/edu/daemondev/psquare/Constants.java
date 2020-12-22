package edu.daemondev.psquare;

import java.util.regex.Pattern;

public class Constants {

    public static final String JWT_TOKEN_SECRET_KEY = "PsquareJWTsecretAPIkey";
    public static final int JWT_TOKEN_VALIDITY = 2 * 60 * 60 * 1000;

    public static final String emailValidationRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final Pattern emailValidationPattern = Pattern.compile(emailValidationRegex);

    public static final String firstnameValidationRegex = "^[a-zA-Z]{3,}$";
    public static final Pattern firstnameValidationPattern = Pattern.compile(firstnameValidationRegex);

    public static final String passwordValidationRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final Pattern passwordValidationPattern = Pattern.compile(passwordValidationRegex);

}
