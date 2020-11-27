package actor;

import java.util.Comparator;

/**
 * Comparator class used for overriding the compare method according to the ascending order of
 * the names of two actors.
 */
public final class ActorsFiltersSort implements Comparator<Actor> {

    @Override
    public int compare(final Actor actor1, final Actor actor2) {
        return actor1.getName().compareTo(actor2.getName());
    }
}
