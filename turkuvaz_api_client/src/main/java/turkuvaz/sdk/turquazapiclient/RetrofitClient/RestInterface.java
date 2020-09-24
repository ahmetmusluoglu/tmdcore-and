package turkuvaz.sdk.turquazapiclient.RetrofitClient;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import turkuvaz.sdk.models.Models.AdsZones.AdsDatum;
import turkuvaz.sdk.models.Models.AllAuthors.AllAuthorsData;
import turkuvaz.sdk.models.Models.AuthorList.AuthorsModel;
import turkuvaz.sdk.models.Models.AuthorsArchive.AuthorsArchiveData;
import turkuvaz.sdk.models.Models.CityList.CityListModel;
import turkuvaz.sdk.models.Models.ColumnistSlider.ColumnistData;
import turkuvaz.sdk.models.Models.ConfigBase.BaseConfigData;
import turkuvaz.sdk.models.Models.ConfigFirstLook.FirstLookData;
import turkuvaz.sdk.models.Models.ConfigShared.SharedConfigData;
import turkuvaz.sdk.models.Models.ExchangeList.ExchangeData;
import turkuvaz.sdk.models.Models.FotoGallerySlider.FotoGallerySliderData;
import turkuvaz.sdk.models.Models.GallerySlider.GallerySliderData;
import turkuvaz.sdk.models.Models.GetVideoModel.VideoModel;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.GalleryModel;
import turkuvaz.sdk.models.Models.LiveStreamToken.LiveToken;
import turkuvaz.sdk.models.Models.LiveStreamToken.VideoToken;
import turkuvaz.sdk.models.Models.MansetSlider.MansetSliderData;
import turkuvaz.sdk.models.Models.NewsList.NewsListData;
import turkuvaz.sdk.models.Models.PhotoGalleryList.PhotoGalleryListData;
import turkuvaz.sdk.models.Models.Series.SeriesModel;
import turkuvaz.sdk.models.Models.SingleNews.SingleNewsModel;
import turkuvaz.sdk.models.Models.SnapNews.SnapNewsData;
import turkuvaz.sdk.models.Models.SonSakika.SonDakikaData;
import turkuvaz.sdk.models.Models.StaticPages.StaticPageData;
import turkuvaz.sdk.models.Models.VideoGalleryList.VideoGalleryListData;

public interface RestInterface {
    /**
     * * * LIVE STREAM TOKEN
     */
    @POST("/token")
    @FormUrlEncoded
    Observable<LiveToken> getLiveToken(@Header("Content-Type") String contentType, @Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);

    @POST("/videotoken")
    @FormUrlEncoded
    Observable<VideoToken> getVideoToken(@Header("Content-Type") String contentType, @Header("Authorization") String Authorization, @Field("Url") String url);

    /**
     * * * ADS ZONES
     */
    @GET("/{path}")
    Observable<ArrayList<AdsDatum>> getAdsZones(@Path(value = "path", encoded = true) String path);


    /**
     * * * PUSH REGISTER - RECEIVED - OPENED
     */
    @GET("/registerDeviceAPNS")
    Observable<String> setRegisterPush(@Query("appRef") String appRef, @Query("bundleId") String bundleId, @Query("did") String did, @Query("tokenId") String tokenId, @Query("dev") boolean dev);


    @GET("/pushOpened")
    Observable<String> setOpenedPush(@Query("appRef") String appRef, @Query("bundleId") String bundleId, @Query("did") String did, @Query("tokenId") String tokenId, @Query("pid") int pid, @Query("dev") boolean dev);


    @GET("config/{id}")
    Observable<FirstLookData> getFirstLookConfig(@Path("id") String id);

    @GET("{path}")
    Observable<SharedConfigData> getSharedConfig(@Path(value = "path", encoded = true) String path);

    @GET("{path}")
    Observable<BaseConfigData> getAndroidConfig(@Path(value = "path", encoded = true) String path);

    @GET("{path}")
    Observable<SeriesModel> getAllCategory(@Path(value = "path", encoded = true) String path);


    @GET("{path}")
    Observable<MansetSliderData> getHeadlineNews(@Path(value = "path", encoded = true) String path, @Query("cid") String cId);


    @GET("{path}")
    Observable<SonDakikaData> getLastMinute(@Path(value = "path", encoded = true) String path);


    @GET("{path}")
    Observable<CityListModel> getCityList(@Path(value = "path", encoded = true) String path);

    @GET("{path}")
    Observable<ExchangeData> getExchangeList(@Path(value = "path", encoded = true) String path, @Query("id") int cityCode);


    /**
     * * * SNAP NEWS 8'LI
     */
    @GET("{path}")
    Observable<SnapNewsData> getSnapNews(@Path(value = "path", encoded = true) String path);


    @GET("{path}")
    Observable<ColumnistData> getColumnistSlider(@Path(value = "path", encoded = true) String path);


    @GET("{path}")
    Observable<GallerySliderData> getVideoGallerySlider(@Path(value = "path", encoded = true) String path);

    @GET("{path}")
    Observable<FotoGallerySliderData> getPhotoGallerySlider(@Path(value = "path", encoded = true) String path);


    @GET("{path}")
    Observable<NewsListData> getNewsList(@Path(value = "path", encoded = true) String path, @Query("cid") String cID, @Query("p") Integer page);


    @GET("{path}")
    Observable<SingleNewsModel> getSingleNews(@Path(value = "path", encoded = true) String path, @Query("id") String id);


    @GET("{apiPath}")
    Observable<VideoModel> getVideoURL(@Path(value = "apiPath", encoded = true) String apiPath, @Query("id") String ID);

    @GET("{apiPath}")
    Observable<GalleryModel> getInfinityGallery(@Path(value = "apiPath", encoded = true) String apiPath, @Query("id") String ID, @Query("p") Integer pageIndex);


    @GET("{path}")
    Observable<AuthorsModel> getAuthorList(@Path(value = "path", encoded = true) String path, @Query("id") String authorID, @Query("p") Integer page);


    @GET("{path}")
    Observable<VideoGalleryListData> getVideoGalleryList(@Path(value = "path", encoded = true) String path, @Query("cid") String cID, @Query("p") Integer page);


    @GET("{path}")
    Observable<PhotoGalleryListData> getPhotoGalleryList(@Path(value = "path", encoded = true) String path, @Query("cid") String cID, @Query("p") Integer page);


    @GET("{path}")
    Observable<AuthorsArchiveData> getAuthorsArchive(@Path(value = "path", encoded = true) String path, @Query("id") String id, @Query("p") Integer page);


    @GET("{path}")
    Observable<AllAuthorsData> getAllAuthors(@Path(value = "path", encoded = true) String path);

    @GET("{path}")
    Observable<ColumnistData> getArticleById(@Path(value = "path", encoded = true) String path, @Query("id") String articleID);

    @GET()
    Observable<StaticPageData> getStaticPages(@Url String url);
}