package turkuvaz.general.apps.podcast.event;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/26/2020.
 */

public class MetadataEvent {
    public final Map<String, String> metadata;

    public MetadataEvent(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Nullable
    public String getSongTitle() {
        return metadata.get("StreamTitle");
    }

    @Nullable
    public String getStationName() {
        return metadata.get("icy-name");
    }

    @Nullable
    public String getStationUrl() {
        return metadata.get("icy-url");
    }

    @Nullable
    public String getGenre() {
        return metadata.get("icy-genre");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        return builder.toString();
    }


    @Override
    public boolean equals(Object other) {
        return false;
    }
}

