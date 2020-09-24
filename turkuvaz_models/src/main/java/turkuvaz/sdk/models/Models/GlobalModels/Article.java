package turkuvaz.sdk.models.Models.GlobalModels;

/**
 * Created by turgay.ulutas on 21/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import turkuvaz.sdk.models.Models.FotoGallerySlider.AlbumMedia;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.FotoGallerySlider.ArticleSourceInfo;
import turkuvaz.sdk.models.Models.Series.Response;

import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.*;

public class Article implements Parcelable {

    @SerializedName("secondaryImageAlternateText")
    @Expose
    private String secondaryImageAlternateText;
    @SerializedName("ArticleSourceType")
    @Expose
    private String articleSourceType;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Spot")
    @Expose
    private String spot;
    @SerializedName("NameForUrl")
    @Expose
    private String nameForUrl;
    @SerializedName("CreatedDateReal")
    @Expose
    private String createdDateReal;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ArticleSourceId")
    @Expose
    private String articleSourceId;
    @SerializedName("SpotShort")
    @Expose
    private String spotShort;
    @SerializedName("ListCounterScript")
    @Expose
    private Object listCounterScript;
    @SerializedName(value = "SurmansetBaslik", alternate = {"HeadlineTitleMainPage", "ShowHeadline"})
    @Expose
    private Boolean surmansetBaslik;
    @SerializedName("primaryImageAlternateText")
    @Expose
    private String primaryImageAlternateText;
    @SerializedName("HideArticleRightColumn")
    @Expose
    private Boolean hideArticleRightColumn;
    @SerializedName("CategoryId")
    @Expose
    private String categoryId;
    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("primaryImage")
    @Expose
    private String primaryImage;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("NameForUrl2")
    @Expose
    private String nameForUrl2;
    @SerializedName("TitleShort")
    @Expose
    private String titleShort;
    @SerializedName("CreatedDateInt")
    @Expose
    private Integer createdDateInt;
    @SerializedName("secondaryImage")
    @Expose
    private String secondaryImage;
    @SerializedName("DetailCounterScript")
    @Expose
    private Object detailCounterScript;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("CanUserWriteComments")
    @Expose
    private Boolean canUserWriteComments;
    @SerializedName("ArticleType")
    @Expose
    private String articleType;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("ArticleId")
    @Expose
    private String articleId;
    @SerializedName("SortOrderCurrent")
    @Expose
    private Integer sortOrderCurrent;
    @SerializedName("QuickImageType")
    @Expose
    private Integer quickImageType;
    @SerializedName("UsedMethod")
    @Expose
    private Boolean usedMethod;
    @SerializedName("CustomerCounterImage")
    @Expose
    private Object customerCounterImage;
    @SerializedName("DetailCounterImage")
    @Expose
    private Object detailCounterImage;
    @SerializedName("ListCounterImage")
    @Expose
    private Object listCounterImage;
    @SerializedName("External")
    @Expose
    private String external;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("OutputDate")
    @Expose
    private String outputDate;
    @SerializedName("UseDetailPaging")
    @Expose
    private Boolean useDetailPaging;

    @SerializedName(value = "SurmansetBaslikKategori", alternate = {"ShowGalleryCategoryTitle", "HeadlineTitleCategory"})
    @Expose
    private Boolean surmansetBaslikKategori;

    /**
     * -------------------------------- ARTICLE VIDEO EXTRA START
     */
    @SerializedName("ReferenceSiteId")
    @Expose
    private Object referenceSiteId;
    @SerializedName("BroadcastKind")
    @Expose
    private String broadcastKind;
    @SerializedName("VideoMobileUrl")
    @Expose
    private String videoMobileUrl;
    @SerializedName("Season")
    @Expose
    private Object season;
    @SerializedName("Episode")
    @Expose
    private Object episode;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("VideoTypeId")
    @Expose
    private Integer videoTypeId;
    @SerializedName("VideoUrl")
    @Expose
    private String videoUrl;
    @SerializedName("VideoSmilUrl")
    @Expose
    private String videoSmilUrl;
    @SerializedName("WebSiteId")
    @Expose
    private String webSiteId;
    @SerializedName("VideoDuration")
    @Expose
    private String videoDuration;
    /** -------------------------------- ARTICLE VIDEO EXTRA END*/


    /**
     * -------------------------------- COLUMNIST EXTRA START
     */
    @SerializedName("TwitterUrl")
    @Expose
    private String twitterUrl;
    @SerializedName("TwitterWidgetId")
    @Expose
    private String twitterWidgetId;
    @SerializedName("FacebookUrl")
    @Expose
    private String facebookUrl;
    @SerializedName("EMail")
    @Expose
    private String eMail;
    /** -------------------------------- COLUMNIST EXTRA END*/


    /**
     * -------------------------------- PHOTO GALLERY EXTRA START
     */
    /*@SerializedName("ShowHeadline")
    @Expose
    private Boolean showHeadline;*/
    @SerializedName("ServerUrl")
    @Expose
    private Object serverUrl;
    @SerializedName("UniqueId")
    @Expose
    private String uniqueId;
    @SerializedName("AlbumMediaCount")
    @Expose
    private Integer albumMediaCount;
    @SerializedName("AlbumMedias")
    @Expose
    private List<AlbumMedia> albumMedias = null;
    /**
     * -------------------------------- PHOTO GALLERY EXTRA END
     */


    @SerializedName("ArticleSourceInfo")
    @Expose
    private ArrayList<ArticleSourceInfo> articleSourceInfo = null;


    private ExternalTypes externalType;


    protected Article(Parcel in) {
        secondaryImageAlternateText = in.readString();
        articleSourceType = in.readString();
        title = in.readString();
        spot = in.readString();
        nameForUrl = in.readString();
        createdDateReal = in.readString();
        description = in.readString();
        articleSourceId = in.readString();
        spotShort = in.readString();
        byte tmpSurmansetBaslik = in.readByte();
        surmansetBaslik = tmpSurmansetBaslik == 0 ? null : tmpSurmansetBaslik == 1;
        primaryImageAlternateText = in.readString();
        byte tmpHideArticleRightColumn = in.readByte();
        hideArticleRightColumn = tmpHideArticleRightColumn == 0 ? null : tmpHideArticleRightColumn == 1;
        categoryId = in.readString();
        source = in.readString();
        primaryImage = in.readString();
        url = in.readString();
        nameForUrl2 = in.readString();
        titleShort = in.readString();
        if (in.readByte() == 0) {
            createdDateInt = null;
        } else {
            createdDateInt = in.readInt();
        }
        secondaryImage = in.readString();
        createdDate = in.readString();
        byte tmpCanUserWriteComments = in.readByte();
        canUserWriteComments = tmpCanUserWriteComments == 0 ? null : tmpCanUserWriteComments == 1;
        articleType = in.readString();
        categoryName = in.readString();
        articleId = in.readString();
        if (in.readByte() == 0) {
            sortOrderCurrent = null;
        } else {
            sortOrderCurrent = in.readInt();
        }
        if (in.readByte() == 0) {
            quickImageType = null;
        } else {
            quickImageType = in.readInt();
        }
        byte tmpUsedMethod = in.readByte();
        usedMethod = tmpUsedMethod == 0 ? null : tmpUsedMethod == 1;
        external = in.readString();
        modifiedDate = in.readString();
        outputDate = in.readString();
        byte tmpUseDetailPaging = in.readByte();
        useDetailPaging = tmpUseDetailPaging == 0 ? null : tmpUseDetailPaging == 1;
        broadcastKind = in.readString();
        videoMobileUrl = in.readString();
        id = in.readString();
        if (in.readByte() == 0) {
            videoTypeId = null;
        } else {
            videoTypeId = in.readInt();
        }
        videoUrl = in.readString();
        videoSmilUrl = in.readString();
        webSiteId = in.readString();
        videoDuration = in.readString();
        twitterUrl = in.readString();
        twitterWidgetId = in.readString();
        facebookUrl = in.readString();
        eMail = in.readString();
        byte tmpShowHeadline = in.readByte();
//        showHeadline = tmpShowHeadline == 0 ? null : tmpShowHeadline == 1;
        uniqueId = in.readString();
        if (in.readByte() == 0) {
            albumMediaCount = null;
        } else {
            albumMediaCount = in.readInt();
        }
        byte tmpSurmansetBaslikKategori = in.readByte();
        surmansetBaslikKategori = tmpSurmansetBaslikKategori == 0 ? null : tmpSurmansetBaslikKategori == 1;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getSecondaryImageAlternateText() {
        return secondaryImageAlternateText;
    }

    public void setSecondaryImageAlternateText(String secondaryImageAlternateText) {
        this.secondaryImageAlternateText = secondaryImageAlternateText;
    }

    public String getArticleSourceType() {
        return articleSourceType;
    }

    public void setArticleSourceType(String articleSourceType) {
        this.articleSourceType = articleSourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public String getNameForUrl() {
        return nameForUrl;
    }

    public void setNameForUrl(String nameForUrl) {
        this.nameForUrl = nameForUrl;
    }

    public String getCreatedDateReal() {
        return createdDateReal;
    }

    public void setCreatedDateReal(String createdDateReal) {
        this.createdDateReal = createdDateReal;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getUseDetailPaging() {
        return useDetailPaging;
    }

    public void setUseDetailPaging(Boolean useDetailPaging) {
        this.useDetailPaging = useDetailPaging;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticleSourceId() {
        return articleSourceId;
    }

    public void setArticleSourceId(String articleSourceId) {
        this.articleSourceId = articleSourceId;
    }

    public String getSpotShort() {
        return spotShort;
    }

    public void setSpotShort(String spotShort) {
        this.spotShort = spotShort;
    }

    public Object getListCounterScript() {
        return listCounterScript;
    }

    public void setListCounterScript(Object listCounterScript) {
        this.listCounterScript = listCounterScript;
    }

    public Boolean getSurmansetBaslik() {
        return surmansetBaslik;
    }

    public void setSurmansetBaslik(Boolean surmansetBaslik) {
        this.surmansetBaslik = surmansetBaslik;
    }

    public String getPrimaryImageAlternateText() {
        return primaryImageAlternateText;
    }

    public void setPrimaryImageAlternateText(String primaryImageAlternateText) {
        this.primaryImageAlternateText = primaryImageAlternateText;
    }

    public Boolean getHideArticleRightColumn() {
        return hideArticleRightColumn;
    }

    public void setHideArticleRightColumn(Boolean hideArticleRightColumn) {
        this.hideArticleRightColumn = hideArticleRightColumn;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameForUrl2() {
        return nameForUrl2;
    }

    public void setNameForUrl2(String nameForUrl2) {
        this.nameForUrl2 = nameForUrl2;
    }

    public String getTitleShort() {
        if (titleShort == null || TextUtils.isEmpty(titleShort)) return title;
        else return titleShort;
    }

    public void setTitleShort(String titleShort) {
        this.titleShort = titleShort;
    }

    public Integer getCreatedDateInt() {
        return createdDateInt;
    }

    public void setCreatedDateInt(Integer createdDateInt) {
        this.createdDateInt = createdDateInt;
    }

    public String getSecondaryImage() {
        return secondaryImage;
    }

    public void setSecondaryImage(String secondaryImage) {
        this.secondaryImage = secondaryImage;
    }

    public Object getDetailCounterScript() {
        return detailCounterScript;
    }

    public void setDetailCounterScript(Object detailCounterScript) {
        this.detailCounterScript = detailCounterScript;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getCanUserWriteComments() {
        return canUserWriteComments;
    }

    public void setCanUserWriteComments(Boolean canUserWriteComments) {
        this.canUserWriteComments = canUserWriteComments;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getSortOrderCurrent() {
        return sortOrderCurrent;
    }

    public void setSortOrderCurrent(Integer sortOrderCurrent) {
        this.sortOrderCurrent = sortOrderCurrent;
    }

    public Integer getQuickImageType() {
        return quickImageType;
    }

    public void setQuickImageType(Integer quickImageType) {
        this.quickImageType = quickImageType;
    }

    public Boolean getUsedMethod() {
        return usedMethod;
    }

    public void setUsedMethod(Boolean usedMethod) {
        this.usedMethod = usedMethod;
    }

    public String getExternal() {
        return external;
    }

    public void setExternal(String external) {
        this.external = external;
    }

    public String getOutputDate() {
        return outputDate;
    }

    public void setOutputDate(String outputDate) {
        this.outputDate = outputDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Object getReferenceSiteId() {
        return referenceSiteId;
    }

    public void setReferenceSiteId(Object referenceSiteId) {
        this.referenceSiteId = referenceSiteId;
    }

    public String getBroadcastKind() {
        return broadcastKind;
    }

    public void setBroadcastKind(String broadcastKind) {
        this.broadcastKind = broadcastKind;
    }

    public String getVideoMobileUrl() {
        return videoMobileUrl;
    }

    public void setVideoMobileUrl(String videoMobileUrl) {
        this.videoMobileUrl = videoMobileUrl;
    }

    public Object getSeason() {
        return season;
    }

    public void setSeason(Object season) {
        this.season = season;
    }

    public Object getEpisode() {
        return episode;
    }

    public void setEpisode(Object episode) {
        this.episode = episode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVideoTypeId() {
        return videoTypeId;
    }

    public void setVideoTypeId(Integer videoTypeId) {
        this.videoTypeId = videoTypeId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoSmilUrl() {
        return videoSmilUrl;
    }

    public void setVideoSmilUrl(String videoSmilUrl) {
        this.videoSmilUrl = videoSmilUrl;
    }

    public String getWebSiteId() {
        return webSiteId;
    }

    public void setWebSiteId(String webSiteId) {
        this.webSiteId = webSiteId;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getTwitterWidgetId() {
        return twitterWidgetId;
    }

    public void setTwitterWidgetId(String twitterWidgetId) {
        this.twitterWidgetId = twitterWidgetId;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

//    public Boolean getShowHeadline() {
//        return showHeadline;
//    }
//
//    public void setShowHeadline(Boolean showHeadline) {
//        this.showHeadline = showHeadline;
//    }

    public Object getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(Object serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getAlbumMediaCount() {
        return albumMediaCount;
    }

    public void setAlbumMediaCount(Integer albumMediaCount) {
        this.albumMediaCount = albumMediaCount;
    }

    public List<AlbumMedia> getAlbumMedias() {
        return albumMedias;
    }

    public void setAlbumMedias(List<AlbumMedia> albumMedias) {
        this.albumMedias = albumMedias;
    }

    public Boolean isSurmansetBaslikKategori() {
        return surmansetBaslikKategori;
    }

    public void setSurmansetBaslikKategori(Boolean surmansetBaslikKategori) {
        this.surmansetBaslikKategori = surmansetBaslikKategori;
    }


    public Object getCustomerCounterImage() {
        return customerCounterImage;
    }

    public void setCustomerCounterImage(Object customerCounterImage) {
        this.customerCounterImage = customerCounterImage;
    }

    public Object getDetailCounterImage() {
        return detailCounterImage;
    }

    public void setDetailCounterImage(Object detailCounterImage) {
        this.detailCounterImage = detailCounterImage;
    }

    public Object getListCounterImage() {
        return listCounterImage;
    }

    public void setListCounterImage(Object listCounterImage) {
        this.listCounterImage = listCounterImage;
    }

    public ArrayList<ArticleSourceInfo> getArticleSourceInfo() {
        return articleSourceInfo;
    }

    public ExternalTypes getExternalType() {
        return external == null ? EMPTY :
                external.contains(ALBUM.getType()) ? ALBUM :
                        external.contains(VIDEO.getType()) ? VIDEO :
                                external.contains(NEWS.getType()) ? NEWS :
                                        external.contains(PHOTO_NEWS.getType()) ? PHOTO_NEWS :
                                                external.contains(ARTICLE_SOURCE.getType()) ? ARTICLE_SOURCE :
                                                        external.contains(LIVE_STREAM.getType()) ? LIVE_STREAM :
                                                                external.contains(EXTERNAL.getType()) ? EXTERNAL :
                                                                        external.contains(INTERNAL.getType()) ? INTERNAL : EMPTY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(secondaryImageAlternateText);
        dest.writeString(articleSourceType);
        dest.writeString(title);
        dest.writeString(spot);
        dest.writeString(nameForUrl);
        dest.writeString(createdDateReal);
        dest.writeString(description);
        dest.writeString(articleSourceId);
        dest.writeString(spotShort);
        dest.writeByte((byte) (surmansetBaslik == null ? 0 : surmansetBaslik ? 1 : 2));
        dest.writeString(primaryImageAlternateText);
        dest.writeByte((byte) (hideArticleRightColumn == null ? 0 : hideArticleRightColumn ? 1 : 2));
        dest.writeString(categoryId);
        dest.writeString(source);
        dest.writeString(primaryImage);
        dest.writeString(url);
        dest.writeString(nameForUrl2);
        dest.writeString(titleShort);
        if (createdDateInt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(createdDateInt);
        }
        dest.writeString(secondaryImage);
        dest.writeString(createdDate);
        dest.writeByte((byte) (canUserWriteComments == null ? 0 : canUserWriteComments ? 1 : 2));
        dest.writeString(articleType);
        dest.writeString(categoryName);
        dest.writeString(articleId);
        if (sortOrderCurrent == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sortOrderCurrent);
        }
        if (quickImageType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quickImageType);
        }
        dest.writeByte((byte) (usedMethod == null ? 0 : usedMethod ? 1 : 2));
        dest.writeString(external);
        dest.writeString(modifiedDate);
        dest.writeString(outputDate);
        dest.writeByte((byte) (useDetailPaging == null ? 0 : useDetailPaging ? 1 : 2));
        dest.writeString(broadcastKind);
        dest.writeString(videoMobileUrl);
        dest.writeString(id);
        if (videoTypeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(videoTypeId);
        }
        dest.writeString(videoUrl);
        dest.writeString(videoSmilUrl);
        dest.writeString(webSiteId);
        dest.writeString(videoDuration);
        dest.writeString(twitterUrl);
        dest.writeString(twitterWidgetId);
        dest.writeString(facebookUrl);
        dest.writeString(eMail);
//        dest.writeByte((byte) (showHeadline == null ? 0 : showHeadline ? 1 : 2));
        dest.writeString(uniqueId);
        if (albumMediaCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(albumMediaCount);
        }

        dest.writeByte((byte) (surmansetBaslikKategori == null ? 0 : surmansetBaslikKategori ? 1 : 2));
    }
}
