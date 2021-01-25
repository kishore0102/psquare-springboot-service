package edu.daemondev.psquare.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.daemondev.psquare.models.UserDetails;

@Repository
@Transactional
public interface UserDetailsRepo extends CrudRepository<UserDetails, Long> {

    List<UserDetails> findAll();

    @Query("select u from UserDetails u where u.email = :email")
    UserDetails getUserDetailsByEmail(String email);

    @Query("select count(u) from UserDetails u where u.email = :email")
    int getCountByEmail(String email);

    @Modifying
    @Query(value = "insert into user_details (email, firstname, lastname, status, passhash) VALUES (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    int addUserDetails(String email, String firstname, String lastname, char status, String passhash);

    @Modifying
    @Query(value = "update user_details set otp = ?2, otpts = ?3, otpvalidator = 0 where email = ?1", nativeQuery = true)
    int updateOTPByEmail(String email, String otp, Timestamp otpts);

    @Modifying
    @Query(value = "update user_details set otp = null, otpts = null, otpvalidator = 0 where email = ?1", nativeQuery = true)
    int resetOTPByEmail(String email);

    @Modifying
    @Query(value = "update user_details set lockcount = lockcount + 1 where email = ?1", nativeQuery = true)
    int incrementUserLock(String email);

    @Modifying
    @Query(value = "update user_details set lockcount = 0 where email = ?1", nativeQuery = true)
    int resetUserLock(String email);

    @Modifying
    @Query(value = "update user_details set status = ?2 where email = ?1", nativeQuery = true)
    int updateStatusByEmail(String email, char status);

}
