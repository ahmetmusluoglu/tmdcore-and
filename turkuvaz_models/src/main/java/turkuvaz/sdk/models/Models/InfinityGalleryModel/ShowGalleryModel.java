package turkuvaz.sdk.models.Models.InfinityGalleryModel;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import java.util.ArrayList;


public class ShowGalleryModel implements Parcelable {
    private String adsCode;
    private String shareDomain;
    private String apiPath;
    private String galleryID;
    private String getVideoById_ApiPath;
    @DrawableRes
    private int logoID;

    private ArrayList galleryIds;


    public ShowGalleryModel(String adsCode, String shareDomain, String apiPath, String galleryID, String getVideoById_ApiPath, int logoID) {
        this.adsCode = adsCode;
        this.shareDomain = shareDomain;
        this.apiPath = apiPath;
        this.galleryID = galleryID;
        this.getVideoById_ApiPath = getVideoById_ApiPath;
        this.logoID = logoID;
    }

    public ShowGalleryModel(String adsCode, String shareDomain, String apiPath, ArrayList<String> galleryIds, String getVideoById_ApiPath, int logoID) {
        this.adsCode = adsCode;
        this.shareDomain = shareDomain;
        this.apiPath = apiPath;
        this.galleryIds = galleryIds;
        this.getVideoById_ApiPath = getVideoById_ApiPath;
        this.logoID = logoID;
    }

    protected ShowGalleryModel(Parcel in) {
        adsCode = in.readString();
        shareDomain = in.readString();
        apiPath = in.readString();
        galleryID = in.readString();
        galleryIds = in.readArrayList(ArrayList.class.getClassLoader());
        getVideoById_ApiPath = in.readString();
        logoID = in.readInt();
    }

    public static final Creator<ShowGalleryModel> CREATOR = new Creator<ShowGalleryModel>() {
        @Override
        public ShowGalleryModel createFromParcel(Parcel in) {
            return new ShowGalleryModel(in);
        }

        @Override
        public ShowGalleryModel[] newArray(int size) {
            return new ShowGalleryModel[size];
        }
    };

    public String getAdsCode() {
        return adsCode;
    }

    public void setAdsCode(String adsCode) {
        this.adsCode = adsCode;
    }

    public String getShareDomain() {
        return shareDomain;
    }

    public void setShareDomain(String shareDomain) {
        this.shareDomain = shareDomain;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getGalleryID() {
        return galleryID;
    }

    public void setGalleryID(String galleryID) {
        this.galleryID = galleryID;
    }

    public String getGetVideoById_ApiPath() {
        return getVideoById_ApiPath;
    }

    public void setGetVideoById_ApiPath(String getVideoById_ApiPath) {
        this.getVideoById_ApiPath = getVideoById_ApiPath;
    }

    public int getLogoID() {
        return logoID;
    }

    public void setLogoID(int logoID) {
        this.logoID = logoID;
    }

    public ArrayList<String> getGalleryIds() {
        return galleryIds;
    }

    public void setGalleryIds(ArrayList<String> galleryIds) {
        this.galleryIds = galleryIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(adsCode);
        parcel.writeString(shareDomain);
        parcel.writeString(apiPath);
        parcel.writeString(galleryID);
        parcel.writeList(galleryIds);
        parcel.writeString(getVideoById_ApiPath);
        parcel.writeInt(logoID);
    }
}
