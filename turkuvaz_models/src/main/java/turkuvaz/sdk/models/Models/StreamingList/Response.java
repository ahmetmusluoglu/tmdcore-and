
package turkuvaz.sdk.models.Models.StreamingList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Comparable<Response> {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("EndTime")
    @Expose
    private String endTime;
    @SerializedName("StartTime")
    @Expose
    private String startTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public int compareTo(Response o) {
        if (getStartTime() == null || o.getStartTime() == null)
            return 0;
        return getStartTime().compareTo(o.getStartTime());
    }
}
