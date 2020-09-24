package turkuvaz.general.apps.podcast.util;

import androidx.annotation.NonNull;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/26/2020.
 */

public class MetaDataDecoder {
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;

    @NonNull
    public static Map<String, String> retrieveMetadata(URL streamUrl) throws Throwable {

        // Connect to url
        URLConnection con = streamUrl.openConnection();
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setReadTimeout(READ_TIMEOUT);
        con.setRequestProperty("icy-metadata", "1");
        con.setRequestProperty("connection", "close");
        con.connect();

        Map<String, String> metadata = new HashMap<>();

        return metadata;
    }

    private static Map<String, String> parseIcyMetadata(String icyMetadataString) {
        Map<String, String> icyMetadata = new HashMap<>();
        return icyMetadata;
    }
}
