package models;

import java.util.ArrayList;

public class Meeting {
    private String id;
    private Event event;
    private ArrayList<User> participants;
    private enum priority{
        HIGH, MEDIUM, LOW
    }
    private String attachment;

    /** todo
     *
     * @param u
     * @return false on unsuccessful addition of participant, true on successful addition of participant
     */
    public Boolean addParticipant(User u){
        return false;
    }

    /** todo
     *
     * @param u
     * @return false on unsuccessful removal of participant, true on successful removal of participant
     */
    public Boolean removeParticipant(User u){
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
