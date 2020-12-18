package edu.daemondev.service.modelRepository;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "notes_data")
@IdClass(NotesData.class)
public class NotesData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "userid", nullable = false)
    private String userid;

    @Id
    @Column(name = "seqnbr", nullable = false)
    private int seqnbr;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private char status;

    @Column(name = "createdat")
    private Timestamp createdat;

    @Column(name = "updatedat")
    private Timestamp updatedat;

    public NotesData() {
    }

    public NotesData(String userid, String topic, String description) {
        this.userid = userid;
        this.topic = topic;
        this.description = description;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getSeqnbr() {
        return seqnbr;
    }

    public void setSeqnbr(int seqnbr) {
        this.seqnbr = seqnbr;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    public Timestamp getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Timestamp updatedat) {
        this.updatedat = updatedat;
    }

    @Override
    public String toString() {
        return "NotesData [createdat=" + createdat + ", description=" + description + ", seqnbr=" + seqnbr + ", status="
                + status + ", topic=" + topic + ", updatedat=" + updatedat + ", userid=" + userid + "]";
    }

}
