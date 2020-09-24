
package turkuvaz.sdk.models.Models.ConfigShared;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("apiGoogleDfpUrl")
    @Expose
    private String apiGoogleDfpUrl;
    @SerializedName("globalConfigUrl")
    @Expose
    private String globalConfigUrl;
    @SerializedName("liveStreamUrl")
    @Expose
    private String liveStreamUrl;
    @SerializedName("livestreamUrls")
    @Expose
    private LivestreamUrls livestreamUrls;
    @SerializedName("basinIlanKurumu")
    @Expose
    private BasinIlanKurumu basinIlanKurumu;
    @SerializedName("contentCss")
    @Expose
    private ContentCss contentCss;
    @SerializedName("appColorSettings")
    @Expose
    private AppColorSettings appColorSettings;
    @SerializedName("gdprkvkk")
    @Expose
    private Gdprkvkk gdprkvkk;

    public String getGlobalConfigUrl() {
        return globalConfigUrl;
    }

    public void setGlobalConfigUrl(String globalConfigUrl) {
        this.globalConfigUrl = globalConfigUrl;
    }

    public LivestreamUrls getLivestreamUrls() {
        return livestreamUrls;
    }

    public void setLivestreamUrls(LivestreamUrls livestreamUrls) {
        this.livestreamUrls = livestreamUrls;
    }

    public BasinIlanKurumu getBasinIlanKurumu() {
        return basinIlanKurumu;
    }

    public void setBasinIlanKurumu(BasinIlanKurumu basinIlanKurumu) {
        this.basinIlanKurumu = basinIlanKurumu;
    }

    public String getApiGoogleDfpUrl() {
        return apiGoogleDfpUrl;
    }

    public void setApiGoogleDfpUrl(String apiGoogleDfpUrl) {
        this.apiGoogleDfpUrl = apiGoogleDfpUrl;
    }

    public ContentCss getContentCss() {
        return contentCss;
    }

    public void setContentCss(ContentCss contentCss) {
        this.contentCss = contentCss;
    }

    public AppColorSettings getAppColorSettings() {
        return appColorSettings;
    }

    public void setAppColorSettings(AppColorSettings appColorSettings) {
        this.appColorSettings = appColorSettings;
    }

    public Gdprkvkk getGdprkvkk() {
        return gdprkvkk;
    }

    public void setGdprkvkk(Gdprkvkk gdprkvkk) {
        this.gdprkvkk = gdprkvkk;
    }

    public void setLiveStreamUrl(String liveStreamUrl) {
        this.liveStreamUrl = liveStreamUrl;
    }

    public String getLiveStreamUrl() {
        return liveStreamUrl;
    }
}
