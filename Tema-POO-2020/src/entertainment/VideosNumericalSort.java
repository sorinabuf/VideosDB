package entertainment;

import java.util.Comparator;
import java.util.Map;

/**
 * Comparator class used for overriding the compare method according to the ascending order of
 * the duration, the numbers of occurrences or the number of views of the videos.
 */
public final class VideosNumericalSort implements Comparator<Map.Entry<String, Integer>> {

    @Override
    public int compare(final Map.Entry<String, Integer> video1,
            final Map.Entry<String, Integer> video2) {
        int result = Integer.compare(video1.getValue(), video2.getValue());
        if (result == 0) {
            result = video1.getKey().compareTo(video2.getKey());
        }
        return result;
    }

}
