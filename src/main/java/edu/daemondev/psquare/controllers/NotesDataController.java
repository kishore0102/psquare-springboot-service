package edu.daemondev.psquare.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.daemondev.psquare.exceptions.PsquareAuthException;
import edu.daemondev.psquare.models.NotesData;
import edu.daemondev.psquare.services.NotesDataServiceImpl;

@RestController
@RequestMapping("/api/notes")
public class NotesDataController {

    @Autowired
    NotesDataServiceImpl notesDataService;

    @RequestMapping(method = RequestMethod.GET, value = "/jwtcheck")
    public ResponseEntity<?> listAllNotes(HttpServletRequest request) {
        System.out.println("inside jwt method");
        return ResponseEntity.ok((String) request.getAttribute("tokenuserid"));
    }

    @PostMapping("/getNotes")
    public ResponseEntity<?> getNotesForUser(HttpServletRequest request, @RequestBody NotesData notes) {
        String userid = notes.getUserid();
        String tokenuserid = (String) request.getAttribute("tokenuserid");
        if (!tokenuserid.equals(userid)) {
            throw new PsquareAuthException("Unauthorized access");
        }
        return ResponseEntity.ok(notesDataService.getNotesByUserid(userid));
    }

    @PostMapping("/addNotes")
    public ResponseEntity<?> addNotesForUser(HttpServletRequest request, @RequestBody NotesData notes) {
        String userid = notes.getUserid();
        String topic = notes.getTopic();
        String description = notes.getDescription();
        String tokenuserid = (String) request.getAttribute("tokenuserid");
        if (!tokenuserid.equals(userid)) {
            throw new PsquareAuthException("Unauthorized access");
        }
        return ResponseEntity.ok(notesDataService.addNotes(userid, topic, description));
    }

    @PostMapping("/updateNotes")
    public ResponseEntity<?> updateNotesForUser(HttpServletRequest request, @RequestBody NotesData notes) {
        String userid = notes.getUserid();
        int seqnbr = notes.getSeqnbr();
        String topic = notes.getTopic();
        String descrpition = notes.getDescription();
        String tokenuserid = (String) request.getAttribute("tokenuserid");
        if (!tokenuserid.equals(userid)) {
            throw new PsquareAuthException("Unauthorized access");
        }
        return ResponseEntity.ok(notesDataService.updateNotes(userid, seqnbr, topic, descrpition));
    }

    @PostMapping("/deleteNotes")
    public ResponseEntity<?> deleteNotesForUser(HttpServletRequest request, @RequestBody NotesData notes) {
        String userid = notes.getUserid();
        int seqnbr = notes.getSeqnbr();
        String tokenuserid = (String) request.getAttribute("tokenuserid");
        if (!tokenuserid.equals(userid)) {
            throw new PsquareAuthException("Unauthorized access");
        }
        return ResponseEntity.ok(notesDataService.deleteNotes(userid, seqnbr));
    }

}
