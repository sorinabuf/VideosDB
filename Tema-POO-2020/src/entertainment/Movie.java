package entertainment;

import java.util.ArrayList;
import java.util.List;

public final class Movie extends Video {
    private final int duration; // duration of the movie
    private final List<Double> ratings; // List of ratings given to the movie by users

    /**
     * Class constructor that sets the fields of a movie.
     */
    public Movie(final String title, final ArrayList<String> cast, final ArrayList<String> genres,
            final int launchyear, final int duration) {
        super(title, launchyear, cast, genres); // super constructor call
        this.duration = duration;
        this.ratings = new ArrayList<>();
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }
}
