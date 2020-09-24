package turkuvaz.general.apps.podcast.event;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/26/2020.
 */

public class AdjustVolumeEvent {
    public final float volume;

    public AdjustVolumeEvent(float volume) {
        this.volume = volume;
    }
}
