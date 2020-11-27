package setinputdata;

import java.util.ArrayList;
import java.util.List;

import fileio.UserInputData;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.Input;

import user.User;
import user.UsersDatabase;

import actor.Actor;
import actor.ActorsDatabase;

import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import entertainment.VideosDatabase;

public class SetInputData {
    private final Input input;

    /**
     * Class constructor that sets the input field.
     */
    public SetInputData(final Input input) {
        this.input = input;
    }

    /**
     * Returns an UsersDatabase object that contains a List of User objects. Every
     * User object copies the values of the fields given through the UserInputData
     * object.
     *
     * @return the new usersDatabase initialized with the given input values
     */
    public UsersDatabase setUsersDatabase() {
        List<UserInputData> usersInputData = input.getUsers();
        List<User> usersList = new ArrayList<User>();

        for (UserInputData user : usersInputData) {
            User newUser = new User(user.getUsername(), user.getSubscriptionType(),
                    user.getHistory(), user.getFavoriteMovies());
            usersList.add(newUser);
        }

        UsersDatabase usersDatabase = new UsersDatabase(usersList);
        return usersDatabase;
    }

    /**
     * Returns an ActorsDatabase object that contains a List of Actor objects. Every
     * Actor object copies the values of the fields given through the ActorInputData
     * object.
     *
     * @return the new actorsDatabase initialized with the given input values
     */
    public ActorsDatabase setActorsDatabase() {
        List<ActorInputData> actorsInputData = input.getActors();
        List<Actor> actorsList = new ArrayList<Actor>();

        for (ActorInputData actor : actorsInputData) {
            Actor newActor = new Actor(actor.getName(), actor.getCareerDescription(),
                    actor.getFilmography(), actor.getAwards());
            actorsList.add(newActor);
        }

        ActorsDatabase actorsDatabase = new ActorsDatabase(actorsList);
        return actorsDatabase;
    }

    /**
     * Returns an VideosDatabase object that contains a List of Video objects, a
     * List of Movie objects and a List of Show objects. Every Movie object copies
     * the values of the fields given through the MovieInputData. Every Show object
     * copies the values of the fields given through the SerialInputData objects.
     * The List of Videos contains both the movies and shows, according to the input
     * order.
     *
     * @return the new videosDatabase initialized with the given input values
     */
    public VideosDatabase setVideosDatabase() {
        List<SerialInputData> serialsInputData = input.getSerials();
        List<MovieInputData> moviesInputData = input.getMovies();
        List<Video> videosList = new ArrayList<Video>();
        List<Movie> moviesList = new ArrayList<Movie>();
        List<Show> showsList = new ArrayList<Show>();

        for (MovieInputData movie : moviesInputData) {
            Movie newMovie = new Movie(movie.getTitle(), movie.getCast(), movie.getGenres(),
                    movie.getYear(), movie.getDuration());
            videosList.add(newMovie);
            moviesList.add(newMovie);
        }

        for (SerialInputData show : serialsInputData) {
            Show newShow = new Show(show.getTitle(), show.getCast(), show.getGenres(),
                    show.getNumberSeason(), show.getSeasons(), show.getYear());
            videosList.add(newShow);
            showsList.add(newShow);
        }

        VideosDatabase videosDatabase = new VideosDatabase(videosList, moviesList, showsList);
        return videosDatabase;
    }
}
