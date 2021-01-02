package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event implements Comparable<Event> {
    private String id;
    private ArrayList<User> participants;
    private Priority priority;
    private String attachment;
    private String name;
    private Reminder reminder;
    private LocalDate date;
    private LocalTime time;
    private int durationMinutes;
    private String description;
    private Location location;
    private User host;

    public Event(ArrayList<User> participants, Priority priority) {
        this.participants = participants;
        this.priority = priority;
    }

    public Event(String name, LocalDate date, LocalTime time, int durationMinutes, Location location) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.durationMinutes = durationMinutes;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getName() {
        return name;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public User getHost() {
        return host;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public void setPriority(Priority level) {
        this.priority = level;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setHost(User host) {
        this.host = host;
    }

    /**
     * todo
     *
     * @param u
     * @return false on unsuccessful addition of participant, true on successful
     *         addition of participant
     */
    public Boolean addParticipant(User u) {
        return false;
    }

    /**
     * todo
     *
     * @param u
     * @return false on unsuccessful removal of participant, true on successful
     *         removal of participant
     */
    public Boolean removeParticipant(User u) {
        return false;
    }

    @Override
    public int compareTo(Event other) {
        if (getDate() == null || other.getDate() == null) {
            return 0;
        }
        return getDate().compareTo(other.getDate());
    }

}
