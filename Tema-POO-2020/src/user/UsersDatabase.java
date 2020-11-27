package user;

import java.util.List;
import java.util.ArrayList;

import fileio.ActionInputData;

public final class UsersDatabase {
    private final List<User> usersList; // List of users given through input

    /**
     * Class constructor that sets the List of users.
     */
    public UsersDatabase(final List<User> usersList) {
        this.usersList = usersList;
    }

    /**
     * Returns a List of User objects that contains the first N most active users.
     * The most active users represent the users with the biggest numbers of valid
     * ratings given. The N number is given by the field number of the action
     * object.
     *
     * @param action the command given at input
     * @return a List of the most active users
     */
    public List<User> sortUsers(final ActionInputData action) {
        List<User> sortedUsers = new ArrayList<User>(usersList); // a copy of the
                                                                 // usersList which
                                                                 // will be sorted
        UserRatingSort sortMethod = new UserRatingSort(); // UserRatingSort object
                                                          // containing the sort
                                                          // method of the users

        // If the given sort type is asc, the sort method remains the default one.
        // Otherwise, the sort method will be reversed for the desc sort type.
        if (action.getSortType().equals("asc")) {
            sortedUsers.sort(sortMethod);
        } else {
            sortedUsers.sort(sortMethod.reversed());
        }

        // The sorted List contains only the users with a number of ratings greater than
        // 0.
        sortedUsers.removeIf((user) -> user.getNrRatings() == 0);

        // The sorted List contains only the first N users if N is smaller than the size
        // of the sorted List of users; otherwise, the whole List will be returned.
        if (sortedUsers.size() > action.getNumber()) {
            sortedUsers = sortedUsers.subList(0, action.getNumber());
        }

        return sortedUsers;
    }

    public List<User> getUsersList() {
        return usersList;
    }
}
