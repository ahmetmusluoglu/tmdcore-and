package turkuvaz.sdk.models.Models.MansetSlider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName(value="articles", alternate={"galleries", "videos"})
    @Expose
    private Articles articles;

    public Articles getArticles() {
        return articles;
    }

    public void setArticles(Articles articles) {
        this.articles = articles;
    }

}
