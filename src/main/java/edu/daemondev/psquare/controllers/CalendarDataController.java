package edu.daemondev.psquare.controllers;

import java.time.OffsetDateTime;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.daemondev.psquare.services.CalendarDataServiceImpl;

@RestController
@RequestMapping("/api/calendar/")
public class CalendarDataController {

	@Autowired
	CalendarDataServiceImpl calendarDataService;

	@GetMapping("/getCalendar")
	public ResponseEntity<?> getCalendarForUser(HttpServletRequest request) {
		String tokenuserid = (String) request.getAttribute("tokenuserid");
		return ResponseEntity.ok(calendarDataService.getCalendarByUserid(tokenuserid));
	}

	@PostMapping("/addCalendar")
	public ResponseEntity<?> addCalendarForUser(HttpServletRequest request, @RequestBody Map<String, Object> calendar) {
		String tokenuserid = (String) request.getAttribute("tokenuserid");
		String title = (String) calendar.get("title");
		String description = (String) calendar.get("description");
		String eventoccurs = (String) calendar.get("eventoccurs");
		OffsetDateTime starttime = OffsetDateTime.parse((CharSequence) calendar.get("starttime"));
		OffsetDateTime endtime = OffsetDateTime.parse((CharSequence) calendar.get("endtime"));
		OffsetDateTime reminder = OffsetDateTime.parse("9999-12-31T00:00:00+00:00");
		return ResponseEntity.ok(calendarDataService.addCalendar(tokenuserid, eventoccurs, title, description,
				starttime, endtime, reminder));
	}

	@PostMapping("/updateCalendar")
	public ResponseEntity<?> updateCalendarForUser(HttpServletRequest request,
			@RequestBody Map<String, Object> calendar) {
		String tokenuserid = (String) request.getAttribute("tokenuserid");
		int seqnbr = (int) calendar.get("seqnbr");
		String title = (String) calendar.get("title");
		String description = (String) calendar.get("description");
		String eventoccurs = (String) calendar.get("eventoccurs");
		OffsetDateTime starttime = OffsetDateTime.parse((CharSequence) calendar.get("starttime"));
		OffsetDateTime endtime = OffsetDateTime.parse((CharSequence) calendar.get("endtime"));
		OffsetDateTime reminder = OffsetDateTime.parse("9999-12-31T00:00:00+00:00");
		return ResponseEntity.ok(calendarDataService.updateCalendar(tokenuserid, seqnbr, eventoccurs, title,
				description, starttime, endtime, reminder));
	}

	@PostMapping("/deleteCalendar")
	public ResponseEntity<?> deleteCalendarForUser(HttpServletRequest request,
			@RequestBody Map<String, Object> calendar) {
		String tokenuserid = (String) request.getAttribute("tokenuserid");
		int seqnbr = (int) calendar.get("seqnbr");
		return ResponseEntity.ok(calendarDataService.deleteCalendar(tokenuserid, seqnbr));
	}

}
