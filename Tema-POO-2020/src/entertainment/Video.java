package entertainment;

import java.util.ArrayList;

/**
 * The super class for Movies and Shows.
 */
public class Video {
    // Fields that are common for both movies and shows.
    private final String title; // title of the video
    private final int launchYear; // launch year of the video
    private final ArrayList<String> cast; // list of actors' names that are part of the video cast
    private final ArrayList<String> genres; // list of genres

    /**
     * Class constructor that sets the fields of a video.
     */
    public Video(final String title, final int launchYear, final ArrayList<String> cast,
            final ArrayList<String> genres) {
        this.title = title;
        this.launchYear = launchYear;
        this.cast = cast;
        this.genres = genres;
    }

    public final String getTitle() {
        return title;
    }

    public final int getLaunchYear() {
        return launchYear;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }
}
