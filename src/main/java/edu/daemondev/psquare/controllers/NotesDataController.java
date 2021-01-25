package edu.daemondev.psquare.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.daemondev.psquare.services.NotesDataServiceImpl;

@RestController
@RequestMapping("/api/notes")
public class NotesDataController {

    @Autowired
    NotesDataServiceImpl notesDataService;

    @GetMapping("/getNotes")
    public ResponseEntity<?> getNotesForUser(HttpServletRequest request) {
        String tokenuserid = (String) request.getAttribute("tokenuserid");
        return ResponseEntity.ok(notesDataService.getNotesByUserid(tokenuserid));
    }

    @RequestMapping(method = RequestMethod.OPTIONS, value = "/getNotes")
    public ResponseEntity<?> getNotesOptions(HttpServletRequest request) {
        System.out.println("request came here to options controller");
        return ResponseEntity.ok(null);
    }

    @PostMapping("/addNotes")
    public ResponseEntity<?> addNotesForUser(HttpServletRequest request, @RequestBody Map<String, Object> notes) {
        String topic = (String) notes.get("topic");
        String description = (String) notes.get("description");
        String tokenuserid = (String) request.getAttribute("tokenuserid");
        return ResponseEntity.ok(notesDataService.addNotes(tokenuserid, topic, description));
    }

    @PostMapping("/updateNotes")
    public ResponseEntity<?> updateNotesForUser(HttpServletRequest request, @RequestBody Map<String, Object> notes) {
        int seqnbr = (int) notes.get("seqnbr");
        String topic = (String) notes.get("topic");
        String descrpition = (String) notes.get("description");
        String tokenuserid = (String) request.getAttribute("tokenuserid");
        return ResponseEntity.ok(notesDataService.updateNotes(tokenuserid, seqnbr, topic, descrpition));
    }

    @PostMapping("/deleteNotes")
    public ResponseEntity<?> deleteNotesForUser(HttpServletRequest request, @RequestBody Map<String, Object> notes) {
        int seqnbr = (int) notes.get("seqnbr");
        String tokenuserid = (String) request.getAttribute("tokenuserid");
        return ResponseEntity.ok(notesDataService.deleteNotes(tokenuserid, seqnbr));
    }

}
