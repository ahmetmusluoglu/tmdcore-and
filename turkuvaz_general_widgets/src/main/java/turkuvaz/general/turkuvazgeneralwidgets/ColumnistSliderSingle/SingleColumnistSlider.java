package turkuvaz.general.turkuvazgeneralwidgets.ColumnistSliderSingle;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.ColumnistSlider.ColumnistData;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by Ahmet MUŞLUOĞLU on 4/6/2020.
 */
public class SingleColumnistSlider extends FrameLayout {
    private RestInterface mRestInterface;
    private SingleColumnistSliderAdapter mAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private RecyclerView recColumnistList;
    private Button mBtnTryAgain;
    private SingleColumnistSliderAdapter.onSelectedNewsListener onSelectedNewsListener;
    private String mApiUrl;

    public SingleColumnistSlider(Context context) {
        super(context);
    }

    public SingleColumnistSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleColumnistSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSelectedNewsListener(SingleColumnistSliderAdapter.onSelectedNewsListener onSelectedNewsListener) {
        this.onSelectedNewsListener = onSelectedNewsListener;
    }

    public void setApiPath(String apiUrl) {
        this.mApiUrl = apiUrl;
    }

    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.single_columnist_slider, this, true);

        this.recColumnistList = findViewById(R.id.recColumnistList);
        this.mBtnTryAgain = findViewById(R.id.mBtnTryAgain);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.recColumnistList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.mAdapter = new SingleColumnistSliderAdapter(this.mResponse, getContext());
        this.mAdapter.setSelectedListener(this.onSelectedNewsListener);

        //Hata oluşması durumunda yeniden dene butonu
        mBtnTryAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getColumnistNews(mApiUrl);
            }
        });

        // Son dakika haberleri alınıyor..
        getColumnistNews(mApiUrl);
    }

    private void getColumnistNews(String apiUrl) {
        mRestInterface.getColumnistSlider(apiUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ColumnistData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ColumnistData sliderData) {
                        if (sliderData != null && sliderData.getData() != null && sliderData.getData().getColumnists() != null && sliderData.getData().getColumnists().getResponse() != null) {
                            mResponse.addAll(sliderData.getData().getColumnists().getResponse());
                            mBtnTryAgain.setVisibility(GONE);

                        } else {
                            mBtnTryAgain.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBtnTryAgain.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onComplete() {

                        if (mAdapter == null || recColumnistList == null)
                            return;
                        recColumnistList.setAdapter(mAdapter);
                    }
                });
    }
}
