package user;

import java.util.Comparator;

public final class UserRatingSort implements Comparator<User> {

    /**
     * The overriden compare method that compares two users by the number of valid
     * ratings given to videos. The second criteria of comparison is the username.
     * The method performs the comparison in an ascending order.
     */
    @Override
    public int compare(final User user1, final User user2) {
        int result = Integer.compare(user1.getNrRatings(), user2.getNrRatings());
        if (result == 0) {
            result = user1.getUsername().compareTo(user2.getUsername());
        }
        return result;
    }
}

