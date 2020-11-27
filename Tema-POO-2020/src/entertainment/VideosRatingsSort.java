package entertainment;

import java.util.Comparator;
import java.util.Map;

/**
 * Comparator class used for overriding the compare method according to the ascending order of
 * the ratings of the videos.
 */
public final class VideosRatingsSort implements Comparator<Map.Entry<String, Double>> {

    @Override
    public int compare(final Map.Entry<String, Double> video1,
            final Map.Entry<String, Double> video2) {
        int result = Double.compare(video1.getValue(), video2.getValue());
        if (result == 0) {
            result = video1.getKey().compareTo(video2.getKey());
        }
        return result;
    }
}

