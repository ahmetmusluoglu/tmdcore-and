package turkuvaz.general.turkuvazgeneralwidgets.LastMinute;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.models.Models.SonSakika.SonDakikaData;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by turgay.ulutas on 13/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class LastMinute extends RelativeLayout implements LastMinuteAdapter.onChangePosition {

    private RecyclerView mRec_lastMinute;
    private RestInterface mRestInterface;
    private LastMinuteAdapter mAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private Timer mTimer;
    private Button mBtn_tryAgain;
    private String mApiPath;
    private String mText;
    private LastMinuteAdapter.onSelectedNewsListener onSelectedNewsListener;
    private ImageButton mBtn_next, mBtn_previous;

    public LastMinute(Context context) {
        super(context);
    }

    public void setOnSelectedNewsListener(LastMinuteAdapter.onSelectedNewsListener onSelectedNewsListener) {
        this.onSelectedNewsListener = onSelectedNewsListener;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public void setApiPath(String apiPath) {
        this.mApiPath = apiPath;
    }

    public LastMinute(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LastMinute(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(21)
    public LastMinute(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.last_minute_main, this, true);

        SnapHelper helper = new LinearSnapHelper();
        this.mRec_lastMinute = findViewById(R.id.rec_sonDakikaList);
        mBtn_next = findViewById(R.id.btn_lastMinuteNext);
        mBtn_previous = findViewById(R.id.btn_lastMinutePrev);
        mBtn_tryAgain = findViewById(R.id.btn_lastMinuteTryAgain);
        TextView mTv_allLastMinute = findViewById(R.id.tv_sonDakika);
        RelativeLayout layLastMinute = findViewById(R.id.layLastMinute);

        // Son dakika bar'ında soldaki ve sağdaki alanlar için hem arkaplan hem de text renkleri atandı. Bu değerler pref'ten alındı. sağdaki textin değeri, LastmİnuteAdapter içinde verildi
        mTv_allLastMinute.setTextColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.LAST_MINUTE_TEXT_LEFT.getType(), "#FFFFFF")));
        mTv_allLastMinute.setBackgroundColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.LAST_MINUTE_BACK_LEFT.getType(), "#FFFFFF")));
        layLastMinute.setBackgroundColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.LAST_MINUTE_BACK_RIGHT.getType(), "#FFFFFF")));

        // son dakika barındaki ileri geri butonlarının rengi de, text2 nin veya text1'in renginden olacak yani text_right veya text_left
        DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_right_arrow), Color.parseColor(Preferences.getString(getContext(), ApiListEnums.LAST_MINUTE_TEXT_RIGHT.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz)))));
        DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_left_arrow), Color.parseColor(Preferences.getString(getContext(), ApiListEnums.LAST_MINUTE_TEXT_RIGHT.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz)))));
        mBtn_next.setImageResource(R.drawable.ic_right_arrow);
        mBtn_previous.setImageResource(R.drawable.ic_left_arrow);

        this.mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        this.mRec_lastMinute.setLayoutManager(this.mLayoutManager);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.mAdapter = new LastMinuteAdapter(this.mResponse, getContext(), getText());
        this.mAdapter.setSelectedListener(this.onSelectedNewsListener);
        this.mAdapter.setOnChangePosition(this);
        helper.attachToRecyclerView(this.mRec_lastMinute);

        this.mRec_lastMinute.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    stopTimer();
                    if (mRec_lastMinute != null)
                        mRec_lastMinute.setOnTouchListener(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        //TODO Bir haber sonraki..
        mBtn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayoutManager.findLastCompletelyVisibleItemPosition() < (mAdapter.getItemCount() - 1)) {
                    mLayoutManager.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
            }
        });

        //TODO Bir haber önceki..
        mBtn_previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayoutManager.findLastCompletelyVisibleItemPosition() != 0) {
                    mLayoutManager.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() - 1);
                }
            }
        });

        //TODO Hata oluşması durumunda yeniden dene butonu
        mBtn_tryAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getSonDakika(mApiPath);
            }
        });

        //TODO Son dakika haberleri alınıyor..
        getSonDakika(mApiPath);

        mTv_allLastMinute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalIntent.openLastMinute(getContext(), mApiPath);
            }
        });

    }

    private void getSonDakika(String apiUrl) {
        mRestInterface.getLastMinute(apiUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SonDakikaData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SonDakikaData sonDakika) {
                        try {
                            if (sonDakika != null && sonDakika.getData() != null && sonDakika.getData().getArticles() != null && sonDakika.getData().getArticles().getResponse() != null) {
                                mBtn_tryAgain.setVisibility(GONE);
                                mResponse.addAll(sonDakika.getData().getArticles().getResponse());
                            } else {
                                mBtn_tryAgain.setVisibility(VISIBLE);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBtn_tryAgain.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        if (mAdapter == null || mRec_lastMinute == null)
                            return;
                        mRec_lastMinute.setAdapter(mAdapter);
                    }
                });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    private void startTimer() {
        stopTimer();
        mTimer = new Timer();
        mTimer.schedule(new LastMinuteTimerTask(), 10000, 10000);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    @Override
    public void onCurrentPosition(int position) {

/*
        if (position == 0 || mResponse.size() == 0 || mResponse.size() == 1) {
            mBtn_previous.setVisibility(GONE);
        } else
            mBtn_previous.setVisibility(VISIBLE);
*/

    }

    private class LastMinuteTimerTask extends TimerTask {
        private boolean isNext = true;

        @Override
        public void run() {
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mLayoutManager == null || mAdapter == null)
                            return;

                        if (mLayoutManager.findLastCompletelyVisibleItemPosition() < (mAdapter.getItemCount() - 1) && isNext) {
                            mLayoutManager.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                        } else {
                            isNext = false;
                        }

                        if (mLayoutManager.findLastCompletelyVisibleItemPosition() != 0 && !isNext) {
                            mLayoutManager.scrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() - 1);

                        } else {
                            isNext = true;
                        }
                    }
                });
            }
        }
    }
}