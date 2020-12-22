package edu.daemondev.psquare.models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_details")
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 2192318323225072967L;

    @Id
    @Column(name = "userid", nullable = false)
    private String userid;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "status", nullable = false)
    private char status;

    @Column(name = "passhash", nullable = false)
    private String passhash;

    @Column(name = "lockcount", nullable = false)
    private String lockcount;

    @Column(name = "otp", nullable = true)
    private String otp;

    @Column(name = "otpts", nullable = true)
    private Timestamp otpts;

    @Column(name = "otpvalidator", nullable = false)
    private int otpvalidator;

    public UserDetails() {
    }

    public UserDetails(String email, String firstname, String lastname, char status, String passhash) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
        this.passhash = passhash;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getPasshash() {
        return passhash;
    }

    public void setPasshash(String passhash) {
        this.passhash = passhash;
    }

    public String getLockcount() {
        return lockcount;
    }

    public void setLockcount(String lockcount) {
        this.lockcount = lockcount;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Timestamp getOtpts() {
        return otpts;
    }

    public void setOtpts(Timestamp otpts) {
        this.otpts = otpts;
    }

    public int getOtpvalidator() {
        return otpvalidator;
    }

    public void setOtpvalidator(int otpvalidator) {
        this.otpvalidator = otpvalidator;
    }

    @Override
    public String toString() {
        return "UserDetails [email=" + email + ", firstname=" + firstname + ", lastname=" + lastname + ", lockcount="
                + lockcount + ", otp=" + otp + ", otpts=" + otpts + ", otpvalidator=" + otpvalidator + ", passhash="
                + passhash + ", status=" + status + ", userid=" + userid + "]";
    }

}
