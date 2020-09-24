
package turkuvaz.sdk.models.Models.GetVideoModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("video")
    @Expose
    private ListVideoHomeAndDetailPage listVideoHomeAndDetailPage;

    public ListVideoHomeAndDetailPage getListVideoHomeAndDetailPage() {
        return listVideoHomeAndDetailPage;
    }

    public void setListVideoHomeAndDetailPage(ListVideoHomeAndDetailPage listVideoHomeAndDetailPage) {
        this.listVideoHomeAndDetailPage = listVideoHomeAndDetailPage;
    }

}
