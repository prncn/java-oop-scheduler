package models;

public class Location {
    private int id;
    private String name;
    private String city;
    private String zip;
    private String street;
    private String streetNr;
    private String building;
    private String roomNr;

    /**
     * Placeholder constructor
     * @param name
     */
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

    public Location(int location_id, String name, String city, String zip, String street, String streetNr, String building,
                    String roomNr) {
        this.id = location_id;
        this.name = name;
        this.city = city;
        this.zip = zip;
        this.street = street;
        this.streetNr = streetNr;
        this.building = building;
        this.roomNr = roomNr;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        this.roomNr = roomNr;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    public void setName(String name) {
        this.name = name;
    }
}
