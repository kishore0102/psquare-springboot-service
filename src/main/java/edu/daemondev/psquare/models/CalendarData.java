package edu.daemondev.psquare.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.*;

@Entity
@Table(name = "calendar_data")
@IdClass(CalendarData.class)
public class CalendarData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "userid", nullable = false)
    private String userid;

    @Id
    @Column(name = "seqnbr", nullable = false)
    private int seqnbr;

    @Column(name = "eventoccurs", nullable = false)
    private String eventoccurs;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "starttime")
    private OffsetDateTime starttime;

    @Column(name = "endtime")
    private OffsetDateTime endtime;

    @Column(name = "reminder")
    private OffsetDateTime reminder;

    public CalendarData() {
    }

    public CalendarData(String userid, int seqnbr, String eventoccurs, String title, String description,
            OffsetDateTime starttime, OffsetDateTime endtime, OffsetDateTime reminder) {
        this.userid = userid;
        this.seqnbr = seqnbr;
        this.eventoccurs = eventoccurs;
        this.title = title;
        this.description = description;
        this.starttime = starttime;
        this.endtime = endtime;
        this.reminder = reminder;
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

    public String getEventoccurs() {
        return eventoccurs;
    }

    public void setEventoccurs(String eventoccurs) {
        this.eventoccurs = eventoccurs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(OffsetDateTime starttime) {
        this.starttime = starttime;
    }

    public OffsetDateTime getEndtime() {
        return endtime;
    }

    public void setEndtime(OffsetDateTime endtime) {
        this.endtime = endtime;
    }

    public OffsetDateTime getReminder() {
        return reminder;
    }

    public void setReminder(OffsetDateTime reminder) {
        this.reminder = reminder;
    }

    @Override
    public String toString() {
        return "CalendarData [description=" + description + ", endtime=" + endtime + ", eventoccurs=" + eventoccurs
                + ", reminder=" + reminder + ", seqnbr=" + seqnbr + ", starttime=" + starttime + ", title=" + title
                + ", userid=" + userid + "]";
    }

}
