package models;

public class Location {
    private String name;
    private String city;
    private String zip;
    private String street;
    private String streetNr;
    private String building;
    private String roomNr;

    public Location(String name) {
        this.name = name;
        city = "";
        street = "";
        streetNr = "";
        zip = "";
        building = "";
        roomNr = "";
    }

    public Location(String name, String city, String zip, String street, String streetNr, String building,
            String roomNr) {
        this.name = name;
        this.city = city;
        this.zip = zip;
        this.street = street;
        this.streetNr = streetNr;
        this.building = building;
        this.roomNr = roomNr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNr() {
        return streetNr;
    }

    public void setStreetNr(String streetNr) {
        this.streetNr = streetNr;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoomNr() {
        return roomNr;
    }

    public void setRoomNr(String roomNr) {
        roomNr = roomNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
