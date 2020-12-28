package models;

public class Location {
    private String city;
    private String street;
    private String streetNr;
    private String zip;
    private String building;
    private String RoomNr;
    private String where;

    public Location(String _where) {
        this.where = _where;
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
        return RoomNr;
    }

    public void setRoomNr(String roomNr) {
        RoomNr = roomNr;
    }

    public String getWhere() { return where; }

    public void setWhere(String where) { this.where = where; }
}
