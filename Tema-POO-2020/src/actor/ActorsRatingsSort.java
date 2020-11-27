package actor;

import java.util.Map;
import java.util.Comparator;
import java.util.ArrayList;

/**
 * Comparator class used for overriding the compare method according to the ascending order of
 * the ratings of the videos in which two actors starred.
 * The second criteria of comparison consists in the ascending order of the names of the actors.
 */
public final class ActorsRatingsSort implements Comparator<Map.Entry<String, ArrayList<Double>>> {

    @Override
    public int compare(final Map.Entry<String, ArrayList<Double>> actor1,
            final Map.Entry<String, ArrayList<Double>> actor2) {
        // Average rating of the videos in which the first actor played.
        double average1 = 0;
        for (Double number : actor1.getValue()) {
            average1 += number;
        }
        average1 = average1 / actor1.getValue().size();

        // Average rating of the videos in which the second actor played.
        double average2 = 0;
        for (Double number : actor2.getValue()) {
            average2 += number;
        }
        average2 = average2 / actor2.getValue().size();

        int result = Double.compare(average1, average2);

        if (result == 0) {
            result = actor1.getKey().compareTo(actor2.getKey());
        }
        return result;
    }
}
