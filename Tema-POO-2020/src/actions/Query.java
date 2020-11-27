package actions;

import fileio.ActionInputData;

import actor.Actor;
import actor.ActorsDatabase;

import entertainment.VideosDatabase;

import user.User;
import user.UsersDatabase;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public final class Query {
    private final ActionInputData action; // the action to be performed

    /**
     * Class constructor that sets the action field.
     */
    public Query(final ActionInputData action) {
        this.action = action;
    }

    /**
     * Returns a message consisting in the first N users sorted by the number of
     * ratings that they offered, where N is given through the current action.
     *
     * @param usersDatabase the database of all users
     * @return a message accordingly
     */
    private String executeQueryUsers(final UsersDatabase usersDatabase) {
        String message = new String("Query result: [");
        List<User> sortedUsers = new ArrayList<User>();

        sortedUsers = usersDatabase.sortUsers(action); // sorted list by the number of given
                                                       // ratings
        for (int i = 0; i < sortedUsers.size() - 1; ++i) {
            message = message + sortedUsers.get(i).getUsername() + ", ";
        }
        if (sortedUsers.size() > 0) {
            message = message + sortedUsers.get(sortedUsers.size() - 1).getUsername() + "]";
        } else {
            message = message + "]";
        }

        return message;
    }

    /**
     * Returns a message consisting in a list of actors based on the type of query
     * selected.
     *
     * @param actorsDatabase the database of all actors
     * @param videosDatabase the database of all videos
     * @return
     */
    private String executeQueryActors(final ActorsDatabase actorsDatabase,
            final VideosDatabase videosDatabase) {
        String message = new String("Query result: [");
        // list of actors sorted by averages
        List<Map.Entry<String, ArrayList<Double>>> sortedActors =
                new ArrayList<Map.Entry<String, ArrayList<Double>>>();
        // list of actors sorted by number of awards
        List<Map.Entry<String, Integer>> sortedAwards =
                new ArrayList<Map.Entry<String, Integer>>();
        // list of actors sorted by filters career description
        List<Actor> filteredActors = new ArrayList<Actor>();

        if (action.getCriteria().equals("average")) {
            sortedActors = actorsDatabase.sortAverages(action, videosDatabase); // sorted list by
                                                                                // ratings
                                                                                // received
            for (int i = 0; i < sortedActors.size() - 1; ++i) {
                message = message + sortedActors.get(i).getKey() + ", ";
            }

            if (sortedActors.size() > 0) {
                message = message + sortedActors.get(sortedActors.size() - 1).getKey() + "]";
            } else {
                message = message + "]";
            }

            actorsDatabase.getAverageRatings().clear();
        } else if (action.getCriteria().equals("awards")) {
            sortedAwards = actorsDatabase.sortAwards(action); // sorted list by number of awards

            for (int i = 0; i < sortedAwards.size() - 1; ++i) {
                message = message + sortedAwards.get(i).getKey() + ", ";
            }

            if (sortedAwards.size() > 0) {
                message = message + sortedAwards.get(sortedAwards.size() - 1).getKey() + "]";
            } else {
                message = message + "]";
            }

            actorsDatabase.getNumberAwards().clear();
        } else {
            filteredActors = actorsDatabase.sortKeyWords(action); // sorted list by keywords
                                                                  // filters
            for (int i = 0; i < filteredActors.size() - 1; ++i) {
                message = message + filteredActors.get(i).getName() + ", ";
            }

            if (filteredActors.size() > 0) {
                message = message + filteredActors.get(filteredActors.size() - 1).getName() + "]";
            } else {
                message = message + "]";
            }

            actorsDatabase.getFilteredActors().clear();
        }
        return message;
    }

    /**
     * Returns a message consisting in a list of videos based on the type of query
     * selected.
     *
     * @param usersDatabase  the database of all users
     * @param videosDatabase the database of all videos
     * @return
     */
    private String executeQueryVideos(final UsersDatabase usersDatabase,
            final VideosDatabase videosDatabase) {
        String message = new String("Query result: [");
        // list of videos sorted by ratings
        List<Map.Entry<String, Double>> sortedRatings =
                new ArrayList<Map.Entry<String, Double>>();
        // list of videos sorted by durations
        List<Map.Entry<String, Integer>> sortedLongestVideos =
                new ArrayList<Map.Entry<String, Integer>>();
        // list of videos sorted by number of occurrences in favorite videos lists
        List<Map.Entry<String, Integer>> sortedFavoriteVideos =
                new ArrayList<Map.Entry<String, Integer>>();
        // list of videos sorted by number of views
        List<Map.Entry<String, Integer>> sortedMostViewedVideos =
                new ArrayList<Map.Entry<String, Integer>>();

        if (action.getCriteria().equals("ratings")) {
            sortedRatings = videosDatabase.sortRatingsVideos(action); // sorted list by ratings

            for (int i = 0; i < sortedRatings.size() - 1; ++i) {
                message = message + sortedRatings.get(i).getKey() + ", ";
            }

            if (sortedRatings.size() > 0) {
                message = message + sortedRatings.get(sortedRatings.size() - 1).getKey() + "]";
            } else {
                message = message + "]";
            }

            videosDatabase.getAverageRatings().clear();
        } else if (action.getCriteria().equals("longest")) {
            sortedLongestVideos = videosDatabase.sortLongestVideos(action); // sorted list by
                                                                            // duration
            for (int i = 0; i < sortedLongestVideos.size() - 1; ++i) {
                message = message + sortedLongestVideos.get(i).getKey() + ", ";
            }

            if (sortedLongestVideos.size() > 0) {
                message = message
                        + sortedLongestVideos.get(sortedLongestVideos.size() - 1).getKey() + "]";
            } else {
                message = message + "]";
            }

            videosDatabase.getDuration().clear();
        } else if (action.getCriteria().equals("favorite")) {
            videosDatabase.setFavoriteVideos(action, usersDatabase);
            sortedFavoriteVideos = videosDatabase.sortFavoriteVideos(action); // sorted list by
                                                                              // number of
                                                                              // occurrences
            for (int i = 0; i < sortedFavoriteVideos.size() - 1; ++i) {
                message = message + sortedFavoriteVideos.get(i).getKey() + ", ";
            }
            if (sortedFavoriteVideos.size() > 0) {
                message = message
                        + sortedFavoriteVideos.get(sortedFavoriteVideos.size() - 1).getKey()
                        + "]";
            } else {
                message = message + "]";
            }

            videosDatabase.getFavorite().clear();
        } else if (action.getCriteria().equals("most_viewed")) {
            videosDatabase.setMostviewedVideos(action, usersDatabase);
            sortedMostViewedVideos = videosDatabase.sortMostViewedVideos(action); // sorted list
                                                                                  // by number of

            for (int i = 0; i < sortedMostViewedVideos.size() - 1; ++i) {
                message = message + sortedMostViewedVideos.get(i).getKey() + ", ";
            }
            if (sortedMostViewedVideos.size() > 0) {
                message = message
                        + sortedMostViewedVideos.get(sortedMostViewedVideos.size() - 1).getKey()
                        + "]";
            } else {
                message = message + "]";
            }

            videosDatabase.getMostViewed().clear();
        }
        return message;
    }

    /**
     * Returns the message of a query.
     */
    public String executeQuery(final UsersDatabase usersDatabase,
            final ActorsDatabase actorsDatabase, final VideosDatabase videosDatabase) {
        String message = new String();
        if (action.getObjectType().equals("users")) {
            message = executeQueryUsers(usersDatabase);
        } else if (action.getObjectType().equals("actors")) {
            message = executeQueryActors(actorsDatabase, videosDatabase);
        } else if (action.getObjectType().equals("movies")) {
            if (action.getCriteria().equals("ratings")) {
                videosDatabase.setAverageRatingsMovies(action);
            } else if (action.getCriteria().equals("longest")) {
                videosDatabase.setDurationMovies(action);
            }
            message = executeQueryVideos(usersDatabase, videosDatabase);
        } else if (action.getObjectType().equals("shows")) {
            if (action.getCriteria().equals("ratings")) {
                videosDatabase.setAverageRatingsShows(action);
            } else if (action.getCriteria().equals("longest")) {
                videosDatabase.setDurationShows(action);
            }
            message = executeQueryVideos(usersDatabase, videosDatabase);
        }
        return message;
    }
}
