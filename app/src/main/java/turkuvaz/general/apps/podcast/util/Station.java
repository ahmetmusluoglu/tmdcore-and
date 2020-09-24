package turkuvaz.general.apps.podcast.util;


public class Station {

    public String radioId;
    public String slogan;
    public String port;
    public String SI;
    public String SO;
    public String name;
    public String streamURL;
    public String imageURL;

    public Station() {
        super();
    }

    public String getId() {
        return radioId;
    }

    public void setId(String radioId) {
        this.radioId = radioId;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSI() {
        return SI;
    }

    public void setSI(String SI) {
        this.SI = SI;
    }

    public String getSO() {
        return SO;
    }

    public void setSO(String SO) {
        this.SO = SO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Station(String radioId, String port, String name, String streamURL, String imageURL, String slogan) {
        super();

    }


    @Override
    public String toString() {
        return name + "(" + streamURL + ")";
    }
}
