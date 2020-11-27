package user;

import java.util.List;

public class UsersDatabase {
    private final List<User> usersList; // List of users given through input

    /**
     * Class constructor that sets the List of users.
     */
    public UsersDatabase(final List<User> usersList) {
        this.usersList = usersList;
    }

    public List<User> getUsersList() {
        return usersList;
    }
}
