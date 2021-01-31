package edu.daemondev.psquare.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.daemondev.psquare.exceptions.PsquareNotFoundException;
import edu.daemondev.psquare.models.NotesData;
import edu.daemondev.psquare.repositories.NotesDataRepo;

@Service
public class NotesDataServiceImpl {

    @Autowired
    NotesDataRepo notesDataRepo;

    public List<NotesData> getAllNotes() throws PsquareNotFoundException {
        return notesDataRepo.findAll();
    }

    public List<NotesData> getNotesByUserid(String userid) throws PsquareNotFoundException {
        return notesDataRepo.getNotesByUserid(userid);
    }

    public NotesData addNotes(String userid, String topic, String description) throws PsquareNotFoundException {
        char activeStatus = 'A';
        int nextSeqnbr = 0;
        try {
            nextSeqnbr = notesDataRepo.getMaxSeqnbrByUserid(userid) + 1;
        } catch (Exception e) {
            nextSeqnbr = 1;
        }
        int sqlcode = notesDataRepo.addNotes(userid, nextSeqnbr, topic, description, activeStatus);
        System.out.println("NotesDataServiceImpl addNotes - sqlcode: " + sqlcode);
        return notesDataRepo.getNotesByUseridAndSeqnbr(userid, nextSeqnbr);
    }

    public NotesData updateNotes(String userid, int seqnbr, String topic, String descrpition)
            throws PsquareNotFoundException {
        int notes = notesDataRepo.countNotesByUseridAndSeqnbr(userid, seqnbr);
        if (notes == 0) {
            throw new PsquareNotFoundException("Note is not available for update");
        }
        try {
            notesDataRepo.updateNotes(userid, seqnbr, topic, descrpition);
            return notesDataRepo.getNotesByUseridAndSeqnbr(userid, seqnbr);
        } catch (Exception err) {
            throw new PsquareNotFoundException("Error while updating notes", err);
        }
    }

    public String deleteNotes(String userid, int seqnbr) throws PsquareNotFoundException {
        int notes = notesDataRepo.countNotesByUseridAndSeqnbr(userid, seqnbr);
        if (notes == 0) {
            throw new PsquareNotFoundException("Note is not available for delete");
        }
        try {
            notesDataRepo.deleteNotes(userid, seqnbr);
            return "Notes deleted successfully";
        } catch (Exception err) {
            throw new PsquareNotFoundException("Error while deleting notes", err);
        }
    }

}
