package turkuvaz.sdk.global.Listeners;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.SparkPlayerViewPopup;
import turkuvaz.sdk.global.SpecialClient;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.GetVideoModel.VideoModel;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.PHOTO_NEWS;

public class SpecialWebListener implements SpecialClient.onSelectedURLListener {
    private Context mContext;
    private ProgressBar mProgress;
    private onStartPopupVideoListener onStartPopupVideoListener;

    public SpecialWebListener(Context context, ProgressBar progress, onStartPopupVideoListener onStartPopupVideoListener) {
        this.mContext = context;
        this.mProgress = progress;
        this.onStartPopupVideoListener = onStartPopupVideoListener;
    }

    @Override
    public void onVideo(String videoURL, String videoCategory, String videoTitle) {
        if (onStartPopupVideoListener != null) {
            RestInterface mRestInterface = ApiClient.getClientApi(mContext).create(RestInterface.class);
            mRestInterface.getVideoURL(ApiListEnums.DETAILS_VIDEO.getApi(mContext), videoURL.toLowerCase()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<VideoModel>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(VideoModel leftMenu) {
                            try {
                                if (leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().size() > 0) {
                                    String VideoSmilUrl = leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getVideoSmilUrl();
                                    String VideoMobileUrl = leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getVideoMobileUrl();
                                    String VideoUrl = leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getVideoUrl();
                                    if (VideoSmilUrl != null && !TextUtils.isEmpty(VideoSmilUrl)) {
                                        onStartPopupVideoListener.onPopupStart(VideoSmilUrl);
                                    } else if (VideoMobileUrl != null && !TextUtils.isEmpty(VideoMobileUrl)) {
                                        onStartPopupVideoListener.onPopupStart(VideoMobileUrl);
                                    } else {
                                        onStartPopupVideoListener.onPopupStart(VideoUrl);
                                    }
                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(mContext, "Video yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            GlobalIntent.openVideoWithID(mContext, videoURL, ApiListEnums.DETAILS_VIDEO.getApi(mContext), "AD_TAG", false);
        }
    }

    @Override
    public void onNews(String newsID) {
        GlobalIntent.openNewsWithID(mContext, ApiListEnums.DETAILS_ARTICLE.getApi(mContext), newsID);
    }

    @Override
    public void onAlbum(String albumID) {
        ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(mContext), ApplicationsWebUrls.getDomain(mContext.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(mContext), albumID, ApiListEnums.DETAILS_VIDEO.getApi(mContext), IconTypes.getIcon(mContext.getApplicationInfo().packageName));
        GlobalIntent.openInfinityGallery(mContext, showGalleryModel, ExternalTypes.ALBUM);
    }

    @Override
    public void onPhotoNews(String photoNewsID, String title, String spot, String url) {
         ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(mContext), ApplicationsWebUrls.getDomain(mContext.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(mContext), photoNewsID, ApiListEnums.DETAILS_VIDEO.getApi(mContext), IconTypes.getIcon(mContext.getApplicationInfo().packageName));
        GlobalIntent.openInfinityGalleryFotoHaber(mContext,
                showGalleryModel2,
                title,
                spot,
                url);
    }

    @Override
    public void onArticle(String articleSourceID) {
        GlobalIntent.openColumnistWithID(mContext, ApiListEnums.DETAILS_ARTICLE.getApi(mContext), articleSourceID);
    }

    @Override
    public void onExternal(String externalURL) {
        GlobalIntent.openExternalBrowser(mContext, externalURL);
    }

    @Override
    public void onInternal(String internalURL) {
        GlobalIntent.openInternalBrowser(mContext, internalURL);
    }

    @Override
    public void onLoad() {
        if (mProgress != null)
            mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onImage(String imageURL) {
        GlobalIntent.openImage(mContext, imageURL);
    }

    @Override
    public void onLiveStream(String liveStreamID) {
        GlobalIntent.openLiveStreamWithURL(mContext, "Canlı Yayın", liveStreamID, ApiListEnums.ADS_PREROLL.getApi(mContext));
    }

    public interface onStartPopupVideoListener {
        void onPopupStart(String videoUrl);
    }
}
