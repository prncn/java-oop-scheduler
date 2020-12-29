package models;

import java.time.LocalDate;

public class Event {
    private String name;
    private LocalDate date;
    private int durationMin;
    private Location location;

    public Event(String name, LocalDate date, int durationMin, Location location){
        this.name = name;
        this.date = date;
        this.durationMin = durationMin;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
