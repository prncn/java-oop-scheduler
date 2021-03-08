package models;

/**
 * The Location class represents a location which a user can create
 * in order to select the location object as location of an event
 */
public class Location {
    /** id of location */
    private int id;
    /** name of location */
    private String name = "";
    /** city of location (optional)*/
    private String city = "";
    /** zip of location (optional) */
    private String zip = "";
    /** street of location (optional) */
    private String street = "";
    /** street number of location (optional) */
    private String streetNr = "";
    /** building of location (optional) */
    private String building = "";
    /** room number of location (optional) */
    private String roomNr = "";

    /**
     * Minimal constructor
     * @param name - name for location
     */
    public Location(String name) {
        this.name = name;
    }

    /**
     *  Constructor for creating a location without ID
     *  @param name - synonym for location
     *  @param city - city of location
     *  @param zip - zip of city
     *  @param street - street in which event takes place
     *  @param streetNr - streetnumber for the street
     *  @param building - descirbes in which building event takes place
     *  @param roomNr - roomNr for event
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
     * @return id for location
     */
    public int getId() {
        return id;
    }

    /**
     * Set location ID
     * @param id id that is given for location
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
     * @param city the city of the location
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
     * @param street - street of location
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
     * @param streetNr - of street of location
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
     * @param zip - zip code of city of location
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
     * @param building - building of location
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
     * @param roomNr - roomNr of location
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
     * @param name - name of location
     */
    public void setName(String name) {
        this.name = name;
    }
}
