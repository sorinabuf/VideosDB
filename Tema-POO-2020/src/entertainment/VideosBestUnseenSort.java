package entertainment;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Comparator class used for overriding the compare method according to the descending order of
 * the ratings of the videos unseen by a user.
 * The second criteria of comparison consists in the ascending order of videos occurrences in the
 * database.
 */
public final class VideosBestUnseenSort implements Comparator<Map.Entry<String, Double>> {

    private final List<Video> videos;

    /**
     * Class constructor that sets the videos database field.
     */
    public VideosBestUnseenSort(final List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public int compare(final Map.Entry<String, Double> el1, final Map.Entry<String, Double> el2) {
        int result = Double.compare(el1.getValue(), el2.getValue());
        result = result * (-1);
        if (result == 0) {
            int video1 = -1; // index of first video in the database
            int video2 = -1; // index of second video in the database
            for (Video video : videos) {
                if (video.getTitle().equals(el1.getKey())) {
                    video1 = videos.indexOf(video);
                }
                if (video.getTitle().equals(el2.getKey())) {
                    video2 = videos.indexOf(video);
                }
            }
            result = Integer.compare(video1, video2);
        }
        return result;
    }
}
