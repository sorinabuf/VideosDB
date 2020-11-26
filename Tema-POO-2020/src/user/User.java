package user;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import entertainment.Movie;
import entertainment.Show;

public final class User {
    private final String username; // name of the user
    private final String subscriptionType; // type of the subscription
    private final Map<String, Integer> history; // List of seen videos
    private final ArrayList<String> favoriteVideos; // List of favorite videos
    private int nrRatings; // number of valid ratings given by the current user
    private final ArrayList<Movie> ratedMovies; // List of movies rated by the user
    private final Map<Show, ArrayList<Integer>> ratedShows; // map of shows rated by
                                                            // the user with the List
                                                            // of ratings per season

    /**
     * Class constructor that sets the user's fields.
     */
    public User(final String username, final String subscriptionType,
            final Map<String, Integer> history, final ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.favoriteVideos = favoriteMovies;
        this.history = history;
        this.ratedMovies = new ArrayList<Movie>();
        this.ratedShows = new HashMap<Show, ArrayList<Integer>>();
        this.nrRatings = 0;
    }

    public int getNrRatings() {
        return nrRatings;
    }

    public void setNrRatings(int nrRatings) {
        this.nrRatings = nrRatings;
    }

    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    public Map<Show, ArrayList<Integer>> getRatedShows() {
        return ratedShows;
    }

    public String getUsername() {
        return username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public ArrayList<String> getFavoriteVideos() {
        return favoriteVideos;
    }
}
