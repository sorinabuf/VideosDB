package entertainment;

import java.util.ArrayList;

public final class Show extends Video {
    private final int numberOfSeasons; // number of seasons of a show
    private final ArrayList<Season> seasons; // the seasons of a show

    /**
     * Class constructor that sets the fields of a show.
     */
    public Show(final String title, final ArrayList<String> cast, final ArrayList<String> genres,
            final int numberOfSeasons, final ArrayList<Season> seasons, final int launchyear) {
        super(title, launchyear, cast, genres); // super constructor call
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}
