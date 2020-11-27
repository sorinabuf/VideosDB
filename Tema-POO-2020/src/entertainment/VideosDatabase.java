package entertainment;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import fileio.ActionInputData;

import user.User;
import user.UsersDatabase;

public final class VideosDatabase {
    private final List<Video> videosList; // list of all videos, both movies and shows
    private final List<Movie> moviesList; // list of all movies
    private final List<Show> showsList; // list of all shows
    private final Map<String, Double> averageRatings; // list used for keeping the average
                                                      // ratings of the videos
    private final Map<String, Integer> duration; // list used for keeping the duration
                                                 // of the videos
    private final Map<String, Integer> favorite; // list used for storing the users most
                                                 // favorite videos
    private final Map<String, Integer> mostViewed; // list used for storing users most
                                                   // viewed videos
    private final Map<String, Integer> popular; // list used for keeping the most popular

    /**
     * Class constructor that sets the fields of the videos database.
     */
    public VideosDatabase(final List<Video> videosList, final List<Movie> moviesList,
            final List<Show> showsList) {
        this.videosList = videosList;
        this.moviesList = moviesList;
        this.showsList = showsList;
        averageRatings = new HashMap<String, Double>();
        duration = new HashMap<String, Integer>();
        favorite = new HashMap<String, Integer>();
        mostViewed = new HashMap<String, Integer>();
        popular = new HashMap<String, Integer>();
    }

    /**
     * Returns true if a video contains the filters given through the input action.
     * Otherwise, returns false.
     *
     * @param video  the video for which the filters verification is done
     * @param action the action that sets the verifying filters
     * @return boolean answer
     */
    private boolean verifyFilters(final Video video, final ActionInputData action) {
        if (action.getFilters().get(0).contains(String.valueOf(video.getLaunchYear()))
                || action.getFilters().get(0).get(0) == null) { // verify the year
            if (video.getGenres().contains(action.getFilters().get(1).get(0))
                    || action.getFilters().get(1).get(0) == null) { // verify the genre
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the average rating for a given movie.
     */
    public void setRatingsMovies(final Movie movie) {
        double average = 0;
        for (Double rating : movie.getRatings()) {
            average += rating;
        }
        if (movie.getRatings().size() != 0) {
            average = average / movie.getRatings().size();
            averageRatings.put(movie.getTitle(), average); // add the rating in the ratings list
        }
    }

    /**
     * Sets the average rating for a given show.
     */
    public void setRatingsShows(final Show show) {
        double sumAverage = 0;
        for (Season season : show.getSeasons()) {
            double average = 0;
            for (Double rating : season.getRatings()) {
                average += rating;
            }
            if (season.getRatings().size() != 0) {
                average = average / season.getRatings().size();
            }
            sumAverage += average;
        }
        if (sumAverage != 0) {
            sumAverage = sumAverage / show.getNumberOfSeasons();
            averageRatings.put(show.getTitle(), sumAverage); // add the rating in the ratings list
        }
    }

    /**
     * Sets the ratings of the movies from movies database that verify the action
     * filters.
     */
    public void setAverageRatingsMovies(final ActionInputData action) {
        for (Movie movie : moviesList) {
            if (verifyFilters(movie, action)) {
                setRatingsMovies(movie);
            }
        }
    }

    /**
     * Sets the ratings of the shows from shows database that verify the action
     * filters.
     */
    public void setAverageRatingsShows(final ActionInputData action) {
        for (Show show : showsList) {
            if (verifyFilters(show, action)) {
                setRatingsShows(show);
            }
        }
    }

    /**
     * Returns a list of videos sorted by the rating.
     *
     * @param action the action for which the videos are sorted
     * @return a list of videos sorted accordingly to the criteria implemented in
     *         the videos numerical comparator
     */
    public List<Map.Entry<String, Double>> sortSearchVideos() {
        // the new sorted list
        List<Map.Entry<String, Double>> sortedVideos = new ArrayList<Map.Entry<String, Double>>();

        for (Map.Entry<String, Double> video : averageRatings.entrySet()) {
            sortedVideos.add(video); // copy the unsorted list in the list designed to be sorted
        }

        VideosRatingsSort sortMethod = new VideosRatingsSort();

        sortedVideos.sort(sortMethod);

        return sortedVideos;
    }

    /**
     * Returns a list of videos sorted by the rating, respectively by the order of
     * occurrence in database.
     *
     * @param action the action for which the videos are sorted
     * @return a list of videos sorted accordingly to the criteria implemented in
     *         the videos best unseen comparator
     */
    public List<Map.Entry<String, Double>> sortRatingsVideos(final ActionInputData action) {
        // the new sorted list
        List<Map.Entry<String, Double>> sortedVideos = new ArrayList<Map.Entry<String, Double>>();

        for (Map.Entry<String, Double> video : averageRatings.entrySet()) {
            sortedVideos.add(video); // copy the unsorted list in the list designed to be sorted
        }

        VideosRatingsSort sortMethod = new VideosRatingsSort();

        // sort the new list according to the ascending/descending criteria
        if (action.getSortType().equals("asc")) {
            sortedVideos.sort(sortMethod);
        } else {
            sortedVideos.sort(sortMethod.reversed());
        }

        // extract the sublist having the size given in the action performed
        if (sortedVideos.size() > action.getNumber()) {
            sortedVideos = sortedVideos.subList(0, action.getNumber());
        }

        return sortedVideos;
    }

    /**
     * Returns a list of videos sorted by the rating.
     *
     * @param action the action for which the videos are sorted
     * @return a list of videos sorted accordingly to the criteria implemented in
     *         the videos ratings comparator
     */
    public List<Map.Entry<String, Double>> sortBestUnseenVideos() {
        List<Map.Entry<String, Double>> sortedBestUnseenVideos =
                new ArrayList<Map.Entry<String, Double>>(); // the new list

        for (Map.Entry<String, Double> video : averageRatings.entrySet()) {
            sortedBestUnseenVideos.add(video); // copy the unsorted list in the list designed to
                                               // be sorted
        }

        VideosBestUnseenSort sortmethod = new VideosBestUnseenSort(videosList);

        sortedBestUnseenVideos.sort(sortmethod);

        return sortedBestUnseenVideos;
    }

    /**
     * Sets the durations of the movies from movies database that verify the action
     * filters.
     */
    public void setDurationMovies(final ActionInputData action) {
        for (Movie movie : moviesList) {
            if (verifyFilters(movie, action)) {
                duration.put(movie.getTitle(), movie.getDuration());
            }
        }
    }

    /**
     * Sets the durations of the shows from shows database that verify the action
     * filters.
     */
    public void setDurationShows(final ActionInputData action) {
        for (Show show : showsList) {
            if (verifyFilters(show, action)) {
                int durationTotal = 0;
                for (Season season : show.getSeasons()) { // for shows, the total duration time
                    durationTotal += season.getDuration(); // will be the duration of all seasons
                }
                duration.put(show.getTitle(), durationTotal);
            }
        }
    }

    /**
     * Returns a list of videos sorted by the duration.
     *
     * @param action the action for which the videos are sorted
     * @return a list of videos sorted accordingly to the criteria implemented in
     *         the videos numerical comparator
     */
    public List<Map.Entry<String, Integer>> sortLongestVideos(final ActionInputData action) {
        List<Map.Entry<String, Integer>> sortedVideos = new ArrayList<Map.Entry<String, Integer>>();

        for (Map.Entry<String, Integer> video : duration.entrySet()) {
            sortedVideos.add(video); // copy the unsorted list in the list designed to
                                     // be sorted
        }

        VideosNumericalSort sortmethod = new VideosNumericalSort();

        // sort the new list according to the ascending/descending criteria
        if (action.getSortType().equals("asc")) {
            sortedVideos.sort(sortmethod);
        } else {
            sortedVideos.sort(sortmethod.reversed());
        }

        // extract the sublist having the size given in the action performed
        if (sortedVideos.size() > action.getNumber()) {
            sortedVideos = sortedVideos.subList(0, action.getNumber());
        }

        return sortedVideos;
    }

    /**
     * Sets for each video the number of occurrences as favorite videos among users'
     * lists.
     *
     * @param action        the action for which the videos are sorted by the number
     *                      of occurrences in the users favorite videos list
     * @param usersDatabase the database of all users
     */
    public void setFavoriteVideos(final ActionInputData action,
            final UsersDatabase usersDatabase) {
        List<Video> copyList; // copy of the movies list or shows list according to the action
                              // object type
        if (action.getObjectType().equals("movies")) {
            copyList = new ArrayList<Video>(moviesList);
        } else {
            copyList = new ArrayList<Video>(showsList);
        }

        for (Video video : copyList) {
            if (verifyFilters(video, action)) {
                for (User user : usersDatabase.getUsersList()) {
                    if (user.getFavoriteVideos().contains(video.getTitle())) {
                        if (favorite.containsKey(video.getTitle())) {
                            favorite.replace(video.getTitle(),
                                    favorite.get(video.getTitle()) + 1); // update the number of
                                                                         // occurrences of the
                                                                         // movie
                        } else {
                            favorite.put(video.getTitle(), 1); // set the number of occurrences
                                                               // of the movie at 1 as it has not
                                                               // been added before
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns a list of videos sorted by the number of occurrences in favorite videos lists.
     *
     * @param action the action for which the videos are sorted
     * @return a list of videos sorted accordingly to the criteria implemented in
     *         the videos numerical comparator
     */
    public List<Map.Entry<String, Integer>> sortFavoriteVideos(final ActionInputData action) {
        List<Map.Entry<String, Integer>> sortedVideos = new ArrayList<Map.Entry<String, Integer>>();

        for (Map.Entry<String, Integer> video : favorite.entrySet()) {
            sortedVideos.add(video); // copy the unsorted list in the list designed to
                                     // be sorted
        }

        VideosNumericalSort sortmethod = new VideosNumericalSort();

        // sort the new list according to the ascending/descending criteria
        if (action.getSortType().equals("asc")) {
            sortedVideos.sort(sortmethod);
        } else {
            sortedVideos.sort(sortmethod.reversed());
        }

        // extract the sublist having the size given in the action performed
        if (sortedVideos.size() > action.getNumber()) {
            sortedVideos = sortedVideos.subList(0, action.getNumber());
        }

        return sortedVideos;
    }

    /**
     * Sets for each video the number of total views.
     *
     * @param action        the action for which the videos are sorted by the number
     *                      of occurrences in the users favorite videos list
     * @param usersDatabase the database of all users
     */
    public void setMostviewedVideos(final ActionInputData action,
            final UsersDatabase usersDatabase) {
        List<Video> copyList; // copy of the movies list or shows list according to the action
                              // object type
        if (action.getObjectType().equals("movies")) {
            copyList = new ArrayList<Video>(moviesList);
        } else {
            copyList = new ArrayList<Video>(showsList);
        }

        for (Video video : copyList) {
            if (verifyFilters(video, action)) {
                for (User user : usersDatabase.getUsersList()) {
                    if (user.getHistory().containsKey(video.getTitle())) {
                        if (mostViewed.containsKey(video.getTitle())) {
                            // update the number of views of a movie by adding the views of
                            // the current user
                            mostViewed.replace(video.getTitle(), mostViewed.get(video.getTitle())
                                    + user.getHistory().get(video.getTitle()));
                        } else {
                            // set the number of views of the movie as the current user's views
                            // as it has not been added before
                            mostViewed.put(video.getTitle(),
                                    user.getHistory().get(video.getTitle()));
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns a list of videos sorted by the number of views.
     *
     * @param action the action for which the videos are sorted
     * @return a list of videos sorted accordingly to the criteria implemented in
     *         the videos numerical comparator
     */
    public List<Map.Entry<String, Integer>> sortMostViewedVideos(final ActionInputData action) {
        List<Map.Entry<String, Integer>> sortedVideos =
                new ArrayList<Map.Entry<String, Integer>>(); // the new sorted list

        for (Map.Entry<String, Integer> video : mostViewed.entrySet()) {
            sortedVideos.add(video); // copy the unsorted list in the list designed to
                                     // be sorted
        }

        VideosNumericalSort sortmethod = new VideosNumericalSort();

        // sort the new list according to the ascending/descending criteria
        if (action.getSortType().equals("asc")) {
            sortedVideos.sort(sortmethod);
        } else {
            sortedVideos.sort(sortmethod.reversed());
        }

        // extract the sublist having the size given in the action performed
        if (sortedVideos.size() > action.getNumber()) {
            sortedVideos = sortedVideos.subList(0, action.getNumber());
        }

        return sortedVideos;
    }

    public List<Video> getVideosList() {
        return videosList;
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public List<Show> getShowsList() {
        return showsList;
    }

    public Map<String, Double> getAverageRatings() {
        return averageRatings;
    }

    public Map<String, Integer> getDuration() {
        return duration;
    }

    public Map<String, Integer> getFavorite() {
        return favorite;
    }

    public Map<String, Integer> getMostViewed() {
        return mostViewed;
    }

    public Map<String, Integer> getPopular() {
        return popular;
    }

}
