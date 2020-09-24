package turkuvaz.general.apps.podcast.event;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/26/2020.
 */
public class PlayerErrorEvent {
    public final String message;

    public PlayerErrorEvent(String message) {
        this.message = message;
    }
}