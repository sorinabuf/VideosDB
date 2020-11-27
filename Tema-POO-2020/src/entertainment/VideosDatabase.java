package entertainment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideosDatabase {
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
