package models;

import java.util.ArrayList;


public class Meeting {
    private String id;
    private Event event;
    private ArrayList<UserAccount> participants;
    private Priority priority;
    private String attachment;

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    public Meeting(Event event, ArrayList<UserAccount> participants, Priority priority) {
        this.event = event;
        this.participants = participants;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }
    public Event getEvent() {
        return event;
    }
    public ArrayList<UserAccount> getParticipants() {
        return participants;
    }
    public Priority getPriority() {
        return this.priority;
    }
    public String getAttachment() {
        return attachment;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
    public void setParticipants(ArrayList<UserAccount> participants) {
        this.participants = participants;
    }
    public void setPriority(Priority level) {
        this.priority = level;
    }
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    /** todo
     *
     * @param u
     * @return false on unsuccessful addition of participant, true on successful addition of participant
     */
    public Boolean addParticipant(User u) {
        return false;
    }
    
    /** todo
     *
     * @param u
     * @return false on unsuccessful removal of participant, true on successful removal of participant
     */
    public Boolean removeParticipant(User u) {
        return false;
    }

}
