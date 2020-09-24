
package turkuvaz.sdk.models.Models.NewsList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName(value = "articles", alternate = {"advertorials", "galleries", "videos", "columnists"})
    @Expose
    private Articles articles;

    @SerializedName("headlines")
    @Expose
    private Articles headlines;

    public Articles getArticles() {
        return articles;
    }

    public void setArticles(Articles articles) {
        this.articles = articles;
    }

    public Articles getHeadlines() {
        return headlines;
    }

    public void setHeadlines(Articles headlines) {
        this.headlines = headlines;
    }
}
