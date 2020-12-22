package models;

public class RegularUser extends User {

    public RegularUser(String id, String username, String password, String email){
        setId(id);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setAdmin(false);
    }

}
