
package turkuvaz.sdk.models.Models.Series;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("GemiusScriptId")
    @Expose
    private String gemiusScriptId;
    @SerializedName("GemiusScriptDetailId")
    @Expose
    private String gemiusScriptDetailId;
    @SerializedName("NameForUrl2")
    @Expose
    private String nameForUrl2;
    @SerializedName("primaryImage")
    @Expose
    private String primaryImage;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("NameForUrl")
    @Expose
    private String nameForUrl;
    @SerializedName("BroadcastSummary")
    @Expose
    private Object broadcastSummary;
    @SerializedName("MainCategoryId")
    @Expose
    private String mainCategoryId;
    @SerializedName("CategorySocials")
    @Expose
    private List<Object> categorySocials = null;
    @SerializedName("SectionConfigs")
    @Expose
    private SectionConfigs sectionConfigs;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("BroadcastSpot")
    @Expose
    private Object broadcastSpot;

    public String getGemiusScriptId() {
        return gemiusScriptId;
    }

    public void setGemiusScriptId(String gemiusScriptId) {
        this.gemiusScriptId = gemiusScriptId;
    }

    public String getGemiusScriptDetailId() {
        return gemiusScriptDetailId;
    }

    public void setGemiusScriptDetailId(String gemiusScriptDetailId) {
        this.gemiusScriptDetailId = gemiusScriptDetailId;
    }

    public String getNameForUrl2() {
        return nameForUrl2;
    }

    public void setNameForUrl2(String nameForUrl2) {
        this.nameForUrl2 = nameForUrl2;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameForUrl() {
        return nameForUrl;
    }

    public void setNameForUrl(String nameForUrl) {
        this.nameForUrl = nameForUrl;
    }

    public Object getBroadcastSummary() {
        return broadcastSummary;
    }

    public void setBroadcastSummary(Object broadcastSummary) {
        this.broadcastSummary = broadcastSummary;
    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public List<Object> getCategorySocials() {
        return categorySocials;
    }

    public void setCategorySocials(List<Object> categorySocials) {
        this.categorySocials = categorySocials;
    }

    public SectionConfigs getSectionConfigs() {
        return sectionConfigs;
    }

    public void setSectionConfigs(SectionConfigs sectionConfigs) {
        this.sectionConfigs = sectionConfigs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getBroadcastSpot() {
        return broadcastSpot;
    }

    public void setBroadcastSpot(Object broadcastSpot) {
        this.broadcastSpot = broadcastSpot;
    }

}
