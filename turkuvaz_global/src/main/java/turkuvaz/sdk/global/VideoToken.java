package turkuvaz.sdk.global;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.sdk.models.Models.LiveStreamToken.LiveToken;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class VideoToken {

    private getVideoComplated videoCompleted;

    private String getUUID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public VideoToken getVideoToken(final String url, final Context context) {
        RestInterface mRestInterfacePush = ApiClient.getClientToken().create(RestInterface.class);
        mRestInterfacePush.getLiveToken("application/x-www-form-urlencoded", "API_USER_53", "A4FF04S1AQYWQOJ", "password")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LiveToken>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LiveToken liveToken) {
                        try {
                            if (liveToken != null && !TextUtils.isEmpty(liveToken.getAccessToken()))
                                getTokenVideo(liveToken.getAccessToken(), url, context);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return this;
    }


    private void getTokenVideo(String bearerToken, String url, final Context context) {
        RestInterface mRestInterfacePush = ApiClient.getClientToken().create(RestInterface.class);
        mRestInterfacePush.getVideoToken("application/x-www-form-urlencoded", "Bearer " + bearerToken, url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<turkuvaz.sdk.models.Models.LiveStreamToken.VideoToken>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(turkuvaz.sdk.models.Models.LiveStreamToken.VideoToken liveToken) {
                        String Url = liveToken.getData().getUrl();
                        Url += "&SessionID=" + getUUID(context);
                        Url += "&StreamGroup=canli-yayin";
                        Url += "&Site=atvavrupa";
                        Url += "&DeviceGroup=android";
                        videoCompleted.onSuccess(Url);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface getVideoComplated {
        void onSuccess(String videoUrl);
    }

    public VideoToken setVideoComplated(getVideoComplated videoComplated) {
        this.videoCompleted = videoComplated;
        return this;
    }
}
