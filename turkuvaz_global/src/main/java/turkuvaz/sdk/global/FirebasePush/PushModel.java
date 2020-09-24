package turkuvaz.sdk.global.FirebasePush;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by ibrahimgunduz on 18.12.2018.
 */

public class PushModel implements Parcelable {
    private static final String TAG = "PushModel";
    private String message;
    private String title;
    private int pid;
    private String typestr;
    private String refid;
    private String u;
    private String img;
    private String excurl;
    private String cid;

    public PushModel() {
    }

    protected PushModel(Parcel in) {
    }

    public static final Creator<PushModel> CREATOR = new Creator<PushModel>() {
        @Override
        public PushModel createFromParcel(Parcel in) {
            return new PushModel(in);
        }

        @Override
        public PushModel[] newArray(int size) {
            return new PushModel[size];
        }
    };

    public PushModel parse(RemoteMessage remoteMessage) {

        return this;
    }

    public PushModel parse(Bundle bundle) {

        return this;
    }

    public String toString() {
        return "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public PushType getTypestr() {
        return PushType.UNDEFINED;
    }

    public void setTypestr(String typestr) {
        this.typestr = typestr;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getExcurl() {
        return excurl;
    }

    public void setExcurl(String excurl) {
        this.excurl = excurl;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}