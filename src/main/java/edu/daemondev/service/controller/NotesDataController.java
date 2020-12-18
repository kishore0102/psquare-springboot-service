package edu.daemondev.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.daemondev.service.modelRepository.NotesData;
import edu.daemondev.service.modelRepository.NotesDataRepo;

@RestController
@RequestMapping("/api/notes")
public class NotesDataController {

    @Autowired
    NotesDataRepo notesDataRepo;

    @RequestMapping(method = RequestMethod.GET, value = "/listall")
    public List<NotesData> listAllNotes() {
        return (List<NotesData>) notesDataRepo.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getNotes")
    public List<NotesData> getNotesForUser(@RequestBody NotesData request) {
        return notesDataRepo.getNotesByUserid(request.getUserid());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addNotes")
    public String addNotesForUser(@RequestBody NotesData request) {
        char activeStatus = 'A';
        try {
            int nextSeqnbr = 0;
            try {
                nextSeqnbr = notesDataRepo.getMaxSeqnbrByUserid(request.getUserid()) + 1;
            } catch (Exception e) {
                nextSeqnbr = 1;
            }

            int sqlcode = notesDataRepo.insertNotes(request.getUserid(), nextSeqnbr, request.getTopic(),
                    request.getDescription(), activeStatus);
            System.out.println("addNotesForUser - sqlcode: " + sqlcode);
            return "addNotesForUser success";
        } catch (Exception e) {
            return "addNotesForUser error";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updateNotes")
    public String updateNotesForUser(@RequestBody NotesData request) {
        try {
            int sqlcode = notesDataRepo.updateNotes(request.getUserid(), request.getSeqnbr(), request.getTopic(),
                    request.getDescription(), request.getStatus());
            System.out.println("updateNotesForUser - sqlcode: " + sqlcode);
            return "updateNotesForUser success";
        } catch (Exception e) {
            return "updateNotesForUser error";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deleteNotes")
    public String deleteNotesForUser(@RequestBody NotesData request) {
        try {
            int sqlcode = notesDataRepo.deleteNotes(request.getUserid(), request.getSeqnbr(), request.getTopic(),
                    request.getDescription(), request.getStatus());
            System.out.println("deleteNotesForUser - sqlcode: " + sqlcode);
            return "deleteNotesForUser success";
        } catch (Exception e) {
            return "deleteNotesForUser error";
        }
    }

}
