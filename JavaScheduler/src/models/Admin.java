package models;

public class Admin extends User {

    public Admin(String id, String username, String password, String email){
        setId(id);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setAdmin(false);
    }

    /** todo
     *
     * @param u User input
     */
    public void accessUserProfile(User u){

    }

    /** todo
     *
     * @param u
     * @return false on unsuccessful deletion, true on successful deletion
     */
    public boolean deleteUser(User u){
        return false;
    }


}
