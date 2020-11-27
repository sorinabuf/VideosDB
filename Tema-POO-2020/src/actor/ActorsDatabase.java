package actor;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

import entertainment.VideosDatabase;
import entertainment.Movie;
import entertainment.Show;
import entertainment.Season;

import fileio.ActionInputData;

public final class ActorsDatabase {
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

    /**
     * Sets the list of average ratings of the videos in which the actors starred.
     *
     * @param videosDatabase the database of all videos
     */
    private void setAaverageRatings(final VideosDatabase videosDatabase) {
        for (Movie movie : videosDatabase.getMoviesList()) { // firstly, traverse the movies
            if (movie.getRatings().size() != 0) {
                // average rating calculated for a movie
                double averageRating = 0;
                for (Double rating : movie.getRatings()) {
                    averageRating = averageRating + rating;
                }
                averageRating = averageRating / movie.getRatings().size();
                // for each actor of the movie cast, update his average ratings list associated
                for (String actor : movie.getCast()) {
                    ArrayList<Double> newRatings; // the new list of ratings
                    if (averageRatings.get(actor) != null) {
                        newRatings = averageRatings.get(actor);
                        newRatings.add(averageRating); // the new list of ratings will contain
                                                       // the old list and the new rating
                        averageRatings.replace(actor, newRatings); // update the ratings list
                    } else {
                        newRatings = new ArrayList<Double>();
                        newRatings.add(averageRating); // the new list of ratings will contain
                                                       // only the new rating
                        averageRatings.put(actor, newRatings); // add the ratings list
                    }
                }
            }
        }

        for (Show show : videosDatabase.getShowsList()) { // secondly, traverse the shows
            double seasonsAverage = 0;
            for (Season season : show.getSeasons()) {
                // average rating calculated for a season of a show
                double averageRating = 0;
                for (Double rating : season.getRatings()) {
                    averageRating = averageRating + rating;
                }
                if (averageRating != 0) {
                    averageRating = averageRating / season.getRatings().size();
                }
                seasonsAverage += averageRating;
            }
            // average rating calculated for a show
            seasonsAverage = seasonsAverage / show.getNumberOfSeasons();
            if (seasonsAverage != 0) {
                // for each actor of the movie cast, update his average ratings list associated
                for (String actor : show.getCast()) {
                    ArrayList<Double> newRatings; // the new list of ratings
                    if (averageRatings.get(actor) != null) {
                        newRatings = averageRatings.get(actor);
                        newRatings.add(seasonsAverage); // the new list of ratings will contain
                                                        // the old list and the new rating
                        averageRatings.replace(actor, newRatings); // update the ratings list
                    } else {
                        newRatings = new ArrayList<Double>();
                        newRatings.add(seasonsAverage); // the new list of ratings will contain
                                                        // only the new rating
                        averageRatings.put(actor, newRatings); // add the ratings list
                    }
                }
            }
        }
    }

    /**
     * Returns a list of actors sorted by the averages of the movies in which they starred.
     *
     * @param action the action for which the averages are sorted
     * @return a list of actors sorted accordingly to the criteria implemented in the
     *         actors ratings comparator
     */
    public List<Map.Entry<String, ArrayList<Double>>> sortAverages(final ActionInputData action,
            final VideosDatabase videosDatabase) {
        List<Map.Entry<String, ArrayList<Double>>> sortedAverages =
                new ArrayList<Map.Entry<String, ArrayList<Double>>>(); // the new sorted list
        setAaverageRatings(videosDatabase); // set the ratings of the movies in which the actors
                                            // starred
        for (Map.Entry<String, ArrayList<Double>> actor : averageRatings.entrySet()) {
            sortedAverages.add(actor); // copy the map entries of the unsorted list in the list
                                       // designed to be sorted
        }

        ActorsRatingsSort sortMethod = new ActorsRatingsSort();

        // sort the new list according to the ascending/descending criteria
        if (action.getSortType().equals("asc")) {
            sortedAverages.sort(sortMethod); // sortmethod.reversed()
        } else {
            sortedAverages.sort(sortMethod.reversed());
        }

        sortedAverages.removeIf((actor) -> actor.getValue().get(0) == 0); // remove actors with
                                                                          // 0 rating
        // extract the sublist having the size given in the action performed
        if (sortedAverages.size() > action.getNumber()) {
            sortedAverages = sortedAverages.subList(0, action.getNumber());
        }

        return sortedAverages;
    }

    /**
     * Sets for each actor who respects the awards filters of the action his total
     * number of awards.
     *
     * @param actorsDatabase the database of all actors
     * @param action         the action for which the numbers of awards are set
     */
    private void setNumberawards(final ActionInputData action) {
        final int filtersIndex = 3; // the index of awards filter among the fields of the action
        for (Actor actor : actorsList) {
            int sumAwards = 0; // used for total sum of awards
            int nrAwards = 0; // used for verifying the awards filters sort
            for (String award : action.getFilters().get(filtersIndex)) {
                if (award.equals("BEST_PERFORMANCE")
                        && actor.getAwards().containsKey(ActorsAwards.BEST_PERFORMANCE)) {
                    nrAwards++;
                } else if (award.equals("BEST_DIRECTOR")
                        && actor.getAwards().containsKey(ActorsAwards.BEST_DIRECTOR)) {
                    nrAwards++;
                } else if (award.equals("PEOPLE_CHOICE_AWARD")
                        && actor.getAwards().containsKey(ActorsAwards.PEOPLE_CHOICE_AWARD)) {
                    nrAwards++;
                } else if (award.equals("BEST_SUPPORTING_ACTOR")
                        && actor.getAwards().containsKey(ActorsAwards.BEST_SUPPORTING_ACTOR)) {
                    nrAwards++;
                } else if (award.equals("BEST_SCREENPLAY")
                        && actor.getAwards().containsKey(ActorsAwards.BEST_SCREENPLAY)) {
                    nrAwards++;
                }
            }
            // verify whether the actor owns the correct number of awards
            if (nrAwards != action.getFilters().get(filtersIndex).size()) {
                nrAwards = 0;
            }
            // if the actor owns the correct number of awards, add the number of awards in
            // the map entry associated to the actor
            if (nrAwards > 0) {
                for (Map.Entry<ActorsAwards, Integer> awardactor : actor.getAwards().entrySet()) {
                    sumAwards += awardactor.getValue();
                    this.numberAwards.put(actor.getName(), sumAwards);
                }
            }
        }
    }

    /**
     * Returns a list of actors sorted by the number of awards.
     *
     * @param action the action for which the numbers of awards are sorted
     * @return a list of actors sorted accordingly to the criteria implemented in the
     *         actors awards comparator
     */
    public List<Map.Entry<String, Integer>> sortAwards(final ActionInputData action) {
        List<Map.Entry<String, Integer>> sortedAwards =
                new ArrayList<Map.Entry<String, Integer>>(); // the new sorted list
        setNumberawards(action); // set the number of awards for each actor according to the
                                 // filters of the action
        for (Map.Entry<String, Integer> actor : numberAwards.entrySet()) {
            sortedAwards.add(actor); // copy the entries of the unsorted map in the list designed
                                     // to be sorted
        }

        ActorsAwardsSort sortMethod = new ActorsAwardsSort();

        // sort the new list according to the ascending/descending criteria
        if (action.getSortType().equals("asc")) {
            sortedAwards.sort(sortMethod);
        } else {
            sortedAwards.sort(sortMethod.reversed());
        }

        return sortedAwards;
    }

    /**
     * Filters the actors containing the keywords given as filters of the action.
     *
     * @param actorsDatabase the database of all actors
     * @param action         the action for which the actors are sorted by
     *                       description
     */
    private void setKeyWords(final ActionInputData action) {
        // for each actor, verify whether his career description contains the keywords
        // given
        final int filtersIndex = 2; // the index of keywords filter among the fields of the action
        for (Actor actor : actorsList) {
            int nrFilters = 0;
            for (String filter : action.getFilters().get(2)) {
                Pattern pattern = Pattern.compile("[ ,!.'(-]" + filter + "[ ,!.')-]",
                        Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(actor.getCareerDescription());

                if (matcher.find()) {
                    nrFilters++; // if the keyword is found, update the number of found filters
                }
            }
            // if the actor career description contains the correct number of keywords, add
            // the actor in the filtered actors list
            if (nrFilters == action.getFilters().get(filtersIndex).size()) {
                filteredActors.add(actor);
            }
        }
    }

    /**
     * Returns a list of actors sorted by keywords.
     *
     * @param action the action for which the actors are sorted
     * @return a list of actors sorted accordingly to the criteria implemented in the
     *         actors filters comparator
     */
    public List<Actor> sortKeyWords(final ActionInputData action) {
        List<Actor> sortedKeyWords = new ArrayList<Actor>(); // the new sorted list
        setKeyWords(action); // set the list of actors who respect the keywords filter
        for (Actor actor : filteredActors) {
            sortedKeyWords.add(actor); // copy the unsorted list in the list designed to be sorted
        }

        ActorsFiltersSort sortMethod = new ActorsFiltersSort();

        // sort the new list according to the ascending/descending criteria
        if (action.getSortType().equals("asc")) {
            sortedKeyWords.sort(sortMethod);
        } else {
            sortedKeyWords.sort(sortMethod.reversed());
        }

        return sortedKeyWords;
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
