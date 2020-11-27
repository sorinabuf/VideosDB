package actions;

import entertainment.VideosDatabase;

import java.util.HashMap;
import fileio.ActionInputData;
import user.UsersDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import entertainment.VideosNumericalSort;
import entertainment.VideosBestUnseenSort;

import user.User;

public final class Recommendation {
    private final ActionInputData action; // the action to be performed

    /**
     * Class constructor that sets the action field.
     */
    public Recommendation(final ActionInputData action) {
        this.action = action;
    }

    /**
     * Returns a message based on the standard recommendation action. There are two
     * possible messages to be displayed: when the user has not seen a video and
     * when the user has seen all the videos from the database. If there is a video
     * that has not been viewed, the first unseen video from the videosdatabase will
     * pe returned.
     * 
     * @param videosDatabase the database of all videos
     * @param userAction     the user for whom the action is performed
     * @return a message accordingly
     */
    private String executeRecommandationStandard(final VideosDatabase videosDatabase,
            final User userAction) {
        String message = new String();
        for (Video video : videosDatabase.getVideosList()) {
            if (!(userAction.getHistory().containsKey(video.getTitle()))) {
                message = "StandardRecommendation result: " + video.getTitle();
                return message;
            }
        }
        message = "StandardRecommendation cannot be applied!";
        return message;
    }

    /**
     * Returns a message based on the standard recommendation action. There are two
     * possible messages to be displayed: when the user has not seen a video and
     * when the user has seen all the videos from the database. The video with the
     * biggest rating unseen by the user will be displayed. If there does not exist
     * this type of video, first unseen video with rating 0 will be returned.
     * 
     * @param videosDatabase the database of all videos
     * @param userAction     the user for whom the action is performed
     * @return a message accordingly
     */
    private String executeRecommendationBestUnseen(final VideosDatabase videosDatabase,
            final User userAction) {
        String message = new String();
        for (Movie movie : videosDatabase.getMoviesList()) {
            videosDatabase.setRatingsMovies(movie);
        }
        for (Show show : videosDatabase.getShowsList()) {
            videosDatabase.setRatingsShows(show);
        }

        // list of videos sorted by ratings and the order of occurrence in the videos
        // database
        List<Map.Entry<String, Double>> sortedvideos = new ArrayList<Map.Entry<String, Double>>();
        sortedvideos = videosDatabase.sortBestUnseenVideos();

        for (int i = 0; i < sortedvideos.size(); ++i) {
            if (!(userAction.getHistory().containsKey(sortedvideos.get(i).getKey()))) {
                videosDatabase.getAverageRatings().clear();
                message = "BestRatedUnseenRecommendation result: " + sortedvideos.get(i).getKey();
                return message;
            }
        }

        for (Video video : videosDatabase.getVideosList()) {
            if (!(userAction.getHistory().containsKey(video.getTitle()))) {
                message = "BestRatedUnseenRecommendation result: " + video.getTitle();
                videosDatabase.getAverageRatings().clear();
                return message;
            }
        }
        videosDatabase.getAverageRatings().clear();
        message = "BestRatedUnseenRecommendation cannot be applied!";
        return message;
    }

    /**
     * Returns a message based on the search recommendation action. There are two
     * possible messages to be displayed: when the user has not seen a video and
     * when the user has seen all the videos from the database. The unseen videos
     * from the most popular genre will be displayed. If all the videos from the
     * respective genre have been seen then it continues with the next most popular
     * genre.
     *
     * @param videosDatabase the database of all videos
     * @param userAction     the user for whom the action is performed
     * @return a message accordingly
     */
    private String executeRecommendationSearch(final VideosDatabase videosDatabase,
            final User userAction) {
        String message = "SearchRecommendation result: [";
        for (Movie movie : videosDatabase.getMoviesList()) {
            videosDatabase.setRatingsMovies(movie);
        }
        for (Show show : videosDatabase.getShowsList()) {
            videosDatabase.setRatingsShows(show);
        }
        ArrayList<String> genremovies = new ArrayList<String>(); // list of movies having the
                                                                 // genre given at input
        for (Video video : videosDatabase.getVideosList()) {
            if (!(video.getGenres().contains(action.getGenre()))) {
                if (videosDatabase.getAverageRatings().containsKey(video.getTitle())) {
                    // removes from the map entry associated to a certain video and rating
                    // a video which does not have the genre given through input action
                    videosDatabase.getAverageRatings().remove(video.getTitle());
                }
            } else {
                if (!(videosDatabase.getAverageRatings().containsKey(video.getTitle()))) {
                    if (!(userAction.getHistory().containsKey(video.getTitle()))) {
                        // add an unseen movie with correct genre
                        genremovies.add(video.getTitle());
                    }
                } else {
                    // removes from the map entry associated to a certain video and rating
                    // a video which has already been seen by the user
                    if (userAction.getHistory().containsKey(video.getTitle())) {
                        videosDatabase.getAverageRatings().remove(video.getTitle());
                    }
                }
            }
        }

        Collections.sort(genremovies); // sort the movies in ascending order by the name

        // the new sorted list of videos by rating
        List<Map.Entry<String, Double>> sortedvideos = new ArrayList<Map.Entry<String, Double>>();
        sortedvideos = videosDatabase.sortSearchVideos();

        for (int i = 0; i < genremovies.size() - 1; ++i) {
            message = message + genremovies.get(i) + ", ";
        }

        if (sortedvideos.size() == 0) {
            if (genremovies.size() > 0) {
                message = message + genremovies.get(genremovies.size() - 1) + "]";
            } else {
                message = "SearchRecommendation cannot be applied!";
            }
            videosDatabase.getAverageRatings().clear();
            return message;
        }

        if (genremovies.size() > 0) {
            message = message + genremovies.get(genremovies.size() - 1) + ", ";
        }

        for (int i = 0; i < sortedvideos.size() - 1; ++i) {
            message = message + sortedvideos.get(i).getKey() + ", ";
        }

        message = message + sortedvideos.get(sortedvideos.size() - 1).getKey() + "]";
        videosDatabase.getAverageRatings().clear();
        return message;
    }

    private String executeRecommendationPopular(final VideosDatabase videosDatabase,
            final User userAction, final UsersDatabase usersDatabase) {
        String message = new String();
        Map<String, Integer> populargenres = new HashMap<String, Integer>();
        for (User user : usersDatabase.getUsersList()) {
            for (Video video : videosDatabase.getVideosList()) {
                if (user.getHistory().containsKey(video.getTitle())) {
                    for (String genre : video.getGenres()) {
                        if (populargenres.containsKey(genre)) {
                            populargenres.replace(genre, populargenres.get(genre)
                                    + user.getHistory().get(video.getTitle()));
                        } else {
                            populargenres.put(genre, user.getHistory().get(video.getTitle()));
                        }
                    }
                }
            }
        }
        VideosNumericalSort sortmethod = new VideosNumericalSort();
        List<Map.Entry<String, Integer>> sortedvideos = new ArrayList<Map.Entry<String, Integer>>();

        for (Map.Entry<String, Integer> video : populargenres.entrySet()) {
            sortedvideos.add(video);
        }
        sortedvideos.sort(sortmethod.reversed());
        for (Map.Entry<String, Integer> genre : sortedvideos) {
            for (Video video : videosDatabase.getVideosList()) {
                if (video.getGenres().contains(genre.getKey())
                        && (!userAction.getHistory().containsKey(video.getTitle()))) {
                    message = "PopularRecommendation result: " + video.getTitle();
                    return message;
                }
            }
        }
        message = "PopularRecommendation cannot be applied!";
        return message;
    }

    /**
     * Returns the video with the biggest number of occurrences in favorite videos
     * lists.
     * 
     * @param videosDatabase the database of all videos
     * @param userAction     the user for whom the action is performed
     * @param usersDatabase  the database of all users
     * @return
     */
    private String executeRecommendationFavorite(final VideosDatabase videosDatabase,
            final User userAction, final UsersDatabase usersDatabase) {
        String message = new String();
        Map<String, Double> popularFavorites = new HashMap<String, Double>();
        for (User user : usersDatabase.getUsersList()) {
            for (String video : user.getFavoriteVideos()) {
                if (popularFavorites.containsKey(video)) {
                    popularFavorites.replace(video, popularFavorites.get(video) + 1);
                } else {
                    popularFavorites.put(video, 1.0);
                }
            }
        }
        VideosBestUnseenSort sortmethod = new VideosBestUnseenSort(
                videosDatabase.getVideosList());
        List<Map.Entry<String, Double>> sortedvideos = new ArrayList<Map.Entry<String, Double>>();

        for (Map.Entry<String, Double> video : popularFavorites.entrySet()) {
            sortedvideos.add(video);
        }

        sortedvideos.sort(sortmethod);

        for (Map.Entry<String, Double> video : sortedvideos) {
            if (!(userAction.getHistory().containsKey(video.getKey()))) {
                message = "FavoriteRecommendation result: " + video.getKey();
                return message;
            }
        }
        message = "FavoriteRecommendation cannot be applied!";
        return message;
    }

    /**
     * Returns the message of a recommendation.
     */
    public String executeRecommandation(final UsersDatabase usersDatabase,
            final VideosDatabase videosDatabase) {
        String message = new String();
        User userAction = null;
        for (User user : usersDatabase.getUsersList()) {
            if (user.getUsername().equals(action.getUsername())) {
                userAction = user; // get the user that matches the action username
                break;
            }
        }
        if (action.getType().equals("standard")) {
            message = executeRecommandationStandard(videosDatabase, userAction);
        } else if (action.getType().equals("best_unseen")) {
            message = executeRecommendationBestUnseen(videosDatabase, userAction);
        } else if (action.getType().equals("search")) {
            if (userAction.getSubscriptionType().equals("PREMIUM")) {
                message = executeRecommendationSearch(videosDatabase, userAction);
            } else {
                videosDatabase.getAverageRatings().clear();
                message = "SearchRecommendation cannot be applied!";
            }
        } else if (action.getType().equals("popular")) {
            if (userAction.getSubscriptionType().equals("PREMIUM")) {
                message = executeRecommendationPopular(videosDatabase, userAction, usersDatabase);
            } else {
                message = "PopularRecommendation cannot be applied!";
            }
        } else if (action.getType().equals("favorite")) {
            if (userAction.getSubscriptionType().equals("PREMIUM")) {
                message = executeRecommendationFavorite(videosDatabase, userAction,
                        usersDatabase);
            } else {
                message = "FavoriteRecommendation cannot be applied!";
            }
        }
        return message;
    }
}
