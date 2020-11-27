package actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorsDatabase {
    private final List<Actor> actorsList; // list of all actors existing in the system
    private final Map<String, ArrayList<Double>> averageRatings; // ratings of the videos where
                                                                 // the actors starred
    private final Map<String, Integer> numberAwards; // numbers of awards associated to the actors
    private final List<Actor> filteredActors; // list of actors who meet certain criteria

    /**
     * Class constructor that sets the actors database fields.
     */
    public ActorsDatabase(final List<Actor> actorsList) {
        this.actorsList = actorsList;
        this.averageRatings = new HashMap<String, ArrayList<Double>>();
        this.numberAwards = new HashMap<String, Integer>();
        this.filteredActors = new ArrayList<Actor>();
    }

    public List<Actor> getActorsList() {
        return actorsList;
    }

    public Map<String, ArrayList<Double>> getAverageRatings() {
        return averageRatings;
    }

    public Map<String, Integer> getNumberAwards() {
        return numberAwards;
    }

    public List<Actor> getFilteredActors() {
        return filteredActors;
    }
}
