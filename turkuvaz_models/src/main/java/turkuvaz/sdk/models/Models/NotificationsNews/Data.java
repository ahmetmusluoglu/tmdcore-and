
package turkuvaz.sdk.models.Models.NotificationsNews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("notifications")
    @Expose
    private Notifications notifications;

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

}
