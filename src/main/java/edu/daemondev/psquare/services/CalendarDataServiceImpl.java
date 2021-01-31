package edu.daemondev.psquare.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.daemondev.psquare.exceptions.PsquareNotFoundException;
import edu.daemondev.psquare.models.CalendarData;
import edu.daemondev.psquare.repositories.CalendarDataRepo;

@Service
public class CalendarDataServiceImpl {

	@Autowired
	CalendarDataRepo calendarDataRepo;

	public List<CalendarData> getCalendarByUserid(String userid) throws PsquareNotFoundException {
		return calendarDataRepo.getCalendarByUserid(userid);
	}

	public CalendarData addCalendar(String userid, String eventoccurs, String title, String description,
			OffsetDateTime starttime, OffsetDateTime endtime, OffsetDateTime reminder) {
		int nextSeqnbr = 0;
		try {
			nextSeqnbr = calendarDataRepo.getMaxSeqnbrByUserid(userid) + 1;
		} catch (Exception e) {
			nextSeqnbr = 1;
		}
		CalendarData newData = new CalendarData(userid, nextSeqnbr, eventoccurs, title, description, starttime, endtime,
				reminder);
		calendarDataRepo.save(newData);
		return newData;
	}

	public CalendarData updateCalendar(String userid, int seqnbr, String eventoccurs, String title, String description,
			OffsetDateTime starttime, OffsetDateTime endtime, OffsetDateTime reminder) {
		int calender = calendarDataRepo.countCalendarByUseridAndSeqnbr(userid, seqnbr);
		if (calender == 0) {
			throw new PsquareNotFoundException("Calendar is not available for update");
		}
		try {
			calendarDataRepo.updateCalendarByUseridAndSeqnbr(userid, seqnbr, eventoccurs, title, description, starttime,
					endtime, reminder);
			return calendarDataRepo.getCalendarByUseridAndSeqnbr(userid, seqnbr);
		} catch (Exception err) {
			throw new PsquareNotFoundException("Error while updating calendar", err);
		}
	}

	public String deleteCalendar(String userid, int seqnbr) throws PsquareNotFoundException {
		int calendar = calendarDataRepo.countCalendarByUseridAndSeqnbr(userid, seqnbr);
		if (calendar == 0) {
			throw new PsquareNotFoundException("calendar is not available for delete");
		}
		try {
			calendarDataRepo.deleteCalendar(userid, seqnbr);
			return "Calendar deleted successfully";
		} catch (Exception err) {
			throw new PsquareNotFoundException("Error while deleting calendar", err);
		}
	}

}