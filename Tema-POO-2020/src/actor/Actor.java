package actor;

import java.util.ArrayList;
import java.util.Map;

public final class Actor {
    private final String name; // name of an actor
    private final String careerDescription; // career description of the actor
    private final ArrayList<String> filmography; // list of videos where the actor played
    private final Map<ActorsAwards, Integer> awards; // awards won by the actor

    /**
     * Class constructor that sets the fields of an actor.
     */
    public Actor(final String name, final String careerDescription,
            final ArrayList<String> filmography,
            final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }
}
