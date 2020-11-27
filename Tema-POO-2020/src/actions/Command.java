package actions;

import fileio.ActionInputData;

import java.util.ArrayList;

import user.User;
import user.UsersDatabase;

import entertainment.VideosDatabase;
import entertainment.Show;
import entertainment.Movie;

public class Command {
    private final ActionInputData action; // the action to be performed

    /**
     * Class constructor that sets the action field.
     */
    public Command(final ActionInputData action) {
        this.action = action;
    }

    /**
     * Returns a message based on the command action. There are three messages
     * according to different situations: when the video has not been seen by the
     * user, when the user has seen the video and already added it to the favorite
     * videos list and when the user has seen the movie but has not marked it as
     * favorite.
     *
     * @param usersDatabase the database of all users
     * @param username      the name of the user for which the command is performed
     * @return a message accordingly
     */
    private String executeCommandFavorite(final UsersDatabase usersDatabase,
            final String username) {
        String message = new String();
        for (User user : usersDatabase.getUsersList()) {
            if ((user.getUsername()).equals(username)) {
                if (user.getHistory().get(action.getTitle()) == null) {
                    message = "error -> " + action.getTitle() + " is not seen";
                } else if (user.getFavoriteVideos().contains(action.getTitle())) {
                    message = "error -> " + action.getTitle() + " is already in favourite list";
                } else {
                    user.getFavoriteVideos().add(action.getTitle());
                    message = "success -> " + action.getTitle() + " was added as favourite";
                }
            }
        }
        return message;
    }

    /**
     * Returns a message based on the command action. There are two messages
     * according to different situations: when the video has not been seen by the
     * user and consequently the total number of views will be 1 and when the video
     * has been viewed by the user and the number of views will be replaced with the
     * old number + 1.
     *
     * @param usersDatabase the database of all users
     * @param username      the name of the user for which the command is performed
     * @return a message accordingly
     */
    private String executeCommandView(final UsersDatabase usersDatabase, final String username) {
        String message = new String();
        for (User user : usersDatabase.getUsersList()) {
            if ((user.getUsername()).equals(username)) {
                if (user.getHistory().get(action.getTitle()) == null) {
                    user.getHistory().put(action.getTitle(), 1);
                    message = "success -> " + action.getTitle()
                            + " was viewed with total views of 1";
                } else {
                    int nrViews = user.getHistory().get(action.getTitle());
                    user.getHistory().replace(action.getTitle(), nrViews + 1);
                    message = "success -> " + action.getTitle()
                            + " was viewed with total views of "
                            + user.getHistory().get(action.getTitle());
                }
            }
        }
        return message;
    }

    /**
     * Returns a message based on the command action. There are two possible
     * scenarios: if the video given is a movie or a show. If the video is a movie
     * there are three messages that may be displayed: when the user has not seen
     * the movie, when the user has already rated the movie and when the user has
     * not rated the movie, so the number of ratings of the user will be increased
     * and the movie will be added to the rated list. Otherwise, the video is a show
     * and the messages are similar, but the ratings will be verified and added per
     * season, not per show.
     *
     * @param usersDatabase  the database of all users
     * @param username       the name of the user for which the command is performed
     * @param videosDatabase the database of all video
     * @return a message accordingly
     */
    private String executeCommandRating(final UsersDatabase usersDatabase, final String username,
            final VideosDatabase videosDatabase) {
        String message = new String();
        int indexUser = -1, indexMovie = -1, indexShow = -1;

        for (User user : usersDatabase.getUsersList()) {
            if (user.getUsername().equals(username)) {
                indexUser = usersDatabase.getUsersList().indexOf(user);
                break; // get index of the user for which the action is performed
            }
        }

        User user = usersDatabase.getUsersList().get(indexUser);

        if (!user.getHistory().containsKey(action.getTitle())) {
            message = "error -> " + action.getTitle() + " is not seen";
            return message;
        } else {
            for (Movie movie : videosDatabase.getMoviesList()) {
                if (movie.getTitle().equals(action.getTitle())) {
                    indexMovie = videosDatabase.getMoviesList().indexOf(movie);
                    break; // get index of the movie for which the action is performed
                }
            }
            if (indexMovie >= 0) { // if the index exists
                Movie movie = videosDatabase.getMoviesList().get(indexMovie);
                if (user.getRatedMovies().contains(movie)) {
                    message = "error -> " + action.getTitle() + " has been already rated";
                    return message;
                } else {
                    usersDatabase.getUsersList().get(indexUser)
                            .setNrRatings(user.getNrRatings() + 1); // set nr of ratings for user
                    // add the respective movie in the rated movies list
                    usersDatabase.getUsersList().get(indexUser).getRatedMovies().add(movie);
                    videosDatabase.getMoviesList().get(indexMovie).getRatings()
                            .add(action.getGrade()); // add the rating of the movie
                    message = "success -> " + action.getTitle() + " was rated with "
                            + action.getGrade() + " by " + username;
                    return message;
                }
            } else {
                for (Show show : videosDatabase.getShowsList()) {
                    if (show.getTitle().equals(action.getTitle())) {
                        indexShow = videosDatabase.getShowsList().indexOf(show);
                        break; // get index of the show for which the action is performed
                    }
                }
                Show show = videosDatabase.getShowsList().get(indexShow);
                if (user.getRatedShows().containsKey(show)) {
                    ArrayList<Integer> seasons = user.getRatedShows().get(show); // old list of
                                                                                 // rated seasons
                    for (Integer integer : seasons) {
                        if (integer == action.getSeasonNumber()) {
                            message = "error -> " + action.getTitle() + " has been already rated";
                            return message;
                        }
                    }
                    seasons.add(action.getSeasonNumber()); // new list of rated seasons of a show
                    usersDatabase.getUsersList().get(indexUser).getRatedShows().replace(show,
                            seasons); // replace the new list of seasons in the rated show map
                    message = "success -> " + action.getTitle() + " was rated with "
                            + action.getGrade() + " by " + username;
                } else {
                    ArrayList<Integer> seasons = new ArrayList<Integer>();
                    seasons.add(action.getSeasonNumber()); // new list of rated seasons of a show
                    usersDatabase.getUsersList().get(indexUser).getRatedShows().put(show,
                            seasons); // add the new list of seasons in the rated show map
                    message = "success -> " + action.getTitle() + " was rated with "
                            + action.getGrade() + " by " + username;
                }
                // set nr of ratings for user
                usersDatabase.getUsersList().get(indexUser).setNrRatings(user.getNrRatings() + 1);
                // add the rating of the season of the show
                videosDatabase.getShowsList().get(indexShow).getSeasons()
                        .get(action.getSeasonNumber() - 1).getRatings().add(action.getGrade());
                return message;
            }
        }
    }

    /**
     * Returns the message of a command.
     */
    public String executeCommand(final UsersDatabase usersDatabase,
            final VideosDatabase videosDatabase) {
        String message = new String();
        String username = action.getUsername();
        if (action.getType().equals("favorite")) {
            message = executeCommandFavorite(usersDatabase, username);
        } else if (action.getType().equals("view")) {
            message = executeCommandView(usersDatabase, username);
        } else {
            message = executeCommandRating(usersDatabase, username, videosDatabase);
        }
        return message;
    }
}
