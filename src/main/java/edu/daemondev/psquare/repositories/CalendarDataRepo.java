package edu.daemondev.psquare.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.daemondev.psquare.models.CalendarData;

@Repository
@Transactional
public interface CalendarDataRepo extends CrudRepository<CalendarData, Long> {

	List<CalendarData> findAll();

	@Query("select n from CalendarData n where n.userid = :userid")
	List<CalendarData> getCalendarByUserid(String userid);

	@Query("select count(*) from CalendarData n where n.userid = :userid and n.seqnbr = :seqnbr")
	int countCalendarByUseridAndSeqnbr(String userid, int seqnbr);

	@Query("select max(n.seqnbr) from CalendarData n where n.userid = :userid")
	int getMaxSeqnbrByUserid(String userid);

	@Query("select n from CalendarData n where n.userid = :userid and n.seqnbr = :seqnbr")
	CalendarData getCalendarByUseridAndSeqnbr(String userid, int seqnbr);

	@Modifying
	@Query(value = "update calendar_data n set eventoccurs = ?3, title = ?4, "
			+ "description = ?5, starttime = ?6, endtime = ?7, reminder = ?8 where userid = ?1 and seqnbr = ?2", nativeQuery = true)
	int updateCalendarByUseridAndSeqnbr(String userid, int seqnbr, String eventoccurs, String title, String description,
			OffsetDateTime starttime, OffsetDateTime endtime, OffsetDateTime reminder);

	@Modifying
	@Query(value = "delete from calendar_data n where userid = ?1 and seqnbr = ?2", nativeQuery = true)
	int deleteCalendar(String userid, int seqnbr);

}
