package actor;

import java.util.Map;
import java.util.Comparator;

/**
 * Comparator class used for overriding the compare method according to the ascending
 * order of the number of awards received by two actors.
 * The second criteria of comparison consists in the ascending order of the names of the
 * actors.
 */
public final class ActorsAwardsSort implements Comparator<Map.Entry<String, Integer>> {

    @Override
    public int compare(final Map.Entry<String, Integer> actor1,
            final Map.Entry<String, Integer> actor2) {

        int result = Integer.compare(actor1.getValue(), actor2.getValue());

        if (result == 0) {
            result = actor1.getKey().compareTo(actor2.getKey());
        }

        return result;
    }
}
