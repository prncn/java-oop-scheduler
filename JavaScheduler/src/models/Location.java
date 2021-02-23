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

    /**
     *  Constructor for creating a location without ID
     */
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

    /**
     *  Constructor for locations that are pulled from the database.
     */
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


    /**
     * Get location ID
     * @return id of location
     */
    public int getId() {
        return id;
    }

    /**
     * Set location ID
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get city of location
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Set city of location
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get street of location
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Set street of location
     * @param street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Get street number of location
     * @return streetNr
     */
    public String getStreetNr() {
        return streetNr;
    }

    /**
     * Set street number of location
     * @param streetNr
     */
    public void setStreetNr(String streetNr) {
        this.streetNr = streetNr;
    }

    /**
     * Get zip code of location
     * @return zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * Set zip code of location
     * @param zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Get building of location
     * @return building
     */
    public String getBuilding() {
        return building;
    }

    /**
     * Set building of location
     * @param building
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * Get room number of location
     * @return roomNr
     */
    public String getRoomNr() {
        return roomNr;
    }

    /**
     * Set room number of location
     * @param roomNr
     */
    public void setRoomNr(String roomNr) {
        this.roomNr = roomNr;
    }

    /**
     * Get name of location
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the name of the location
     * @return name
     */
    public String toString() {
        return getName();
    }

    /**
     * Set name of location
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
