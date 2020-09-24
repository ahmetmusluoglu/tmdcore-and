package turkuvaz.general.apps.podcast.event;

import turkuvaz.general.apps.podcast.util.Station;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/25/2020.
 */
public class DatabaseEvent {
    public final Station station;

    public enum Operation {
        CREATE_STATION, DELETE_STATION, UPDATE_STATION
    }

    public final Operation operation;

    public DatabaseEvent(Operation operation, Station station) {
        this.operation = operation;
        this.station = station;
    }
}
