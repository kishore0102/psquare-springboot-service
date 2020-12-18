package edu.daemondev.service.modelRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotesDataRepo extends CrudRepository<NotesData, Long> {

    @Query("select n from NotesData n where n.userid = :userid")
    List<NotesData> getNotesByUserid(String userid);

    @Query("select max(n.seqnbr) from NotesData n where n.userid = :userid")
    int getMaxSeqnbrByUserid(String userid);

    @Modifying
    @Query(value = "insert into notes_data (userid, seqnbr, topic, description, status) VALUES (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    int insertNotes(String userid, int seqnbr, String topic, String description, char status);

    @Modifying
    @Query(value = "update notes_data n set topic = ?3, description =  ?4 where userid = ?1 and seqnbr = ?2", nativeQuery = true)
    int updateNotes(String userid, int seqnbr, String topic, String description, char status);

    @Modifying
    @Query(value = "delete from notes_data n where userid = ?1 and seqnbr = ?2", nativeQuery = true)
    int deleteNotes(String userid, int seqnbr, String topic, String description, char status);

}
