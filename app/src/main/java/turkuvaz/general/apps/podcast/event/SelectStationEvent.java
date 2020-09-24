package turkuvaz.general.apps.podcast.event;


import turkuvaz.general.apps.podcast.util.Station;

public class SelectStationEvent {
    public final Station station;

    public SelectStationEvent(Station station) {
        this.station = station;
    }
}
