package turkuvaz.general.turkuvazgeneralactivitys.ColumnistsDetailSlider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.GeneralUtils.LoopViewPager;
import turkuvaz.sdk.galleryslider.MediaPlayer.NativeMediaPlayer;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.Listeners.SpecialWebListener;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.global.SparkPlayerViewPopup;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;

public class AllColumnistsActivity extends AppCompatActivity implements SpecialWebListener.onStartPopupVideoListener, View.OnTouchListener, ColumnistPaperAdapter.OnScrllListener {

    private PopupWindow popupWindow = null;
    private View popupView = null;
    private SparkPlayerViewPopup sparkPlayerViewPopup = null;
    private static final int MAX_CLICK_DISTANCE = 15;
    private static final int MAX_CLICK_DURATION = 1000;
    private long pressStartTime;
    private int pressedX;
    private int pressedY;
    private int offsetX, offsetY;
    private int actionBarHeight = 0;
    private boolean allArticleBtnIsShow;

    private LoopViewPager viewPager;
    private CirclePageIndicator circleIndicator;
    private RelativeLayout RLPageIndicator;
    private RelativeLayout toolbar;
    private RelativeLayout rlPrev;
    private RelativeLayout rlNext;
    private ArrayList<Article> articles;
    private int currPosition = 0;
    private RelativeLayout rlBack;
    private TextView txtTitle;
    private ImageView imgBack;
    private LinearLayout llLoading;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(getApplicationContext(), ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            setContentView(R.layout.activity_all_columnists);

            viewPager = findViewById(R.id.viewpager);
            rlPrev = findViewById(R.id.rlPrev);
            rlNext = findViewById(R.id.rlNext);
            circleIndicator = findViewById(R.id.circleIndicator);
            RLPageIndicator = findViewById(R.id.RLPageIndicator);
            txtTitle = findViewById(R.id.txtTitle);
            toolbar = findViewById(R.id.toolbar);
            rlBack = findViewById(R.id.rlBack);
            imgBack = findViewById(R.id.imgBack);
            llLoading = findViewById(R.id.llLoading);
            rlBack.setOnClickListener(view1 -> finish());
            rlPrev.setOnClickListener(view1 -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true));
            rlNext.setOnClickListener(view1 -> viewPager.setCurrentItem(viewPager.getCurrentItem() == (articles.size() - 1) ? 0 : (viewPager.getCurrentItem() + 1), true));//sonda iken en başa gelmesi için

            viewPager.setScroll(true);
            llLoading.setVisibility(View.VISIBLE);
            int screenHeight = Preferences.getInt(getApplicationContext(), SettingsTypes.SCREEN_HEIGHT.getType(), 800);
            RelativeLayout.LayoutParams relativeParamsNext = (RelativeLayout.LayoutParams) rlNext.getLayoutParams();
            RelativeLayout.LayoutParams relativeParamsPrev = (RelativeLayout.LayoutParams) rlPrev.getLayoutParams();
            relativeParamsNext.topMargin = screenHeight / 2;
            relativeParamsPrev.topMargin = screenHeight / 2;
            rlNext.setLayoutParams(relativeParamsNext);
            rlPrev.setLayoutParams(relativeParamsPrev);
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(getApplicationContext(), ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            onPageChangeListener();

            new LoadColumnistsPages().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadColumnistsPages extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(getApplicationContext(), ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey("allColumnists")) {
                articles = new Gson().fromJson(bundle.getString("allColumnists"), new TypeToken<ArrayList<Article>>() {
                }.getType());
                allArticleBtnIsShow = bundle.getBoolean("allArticleBtnIsShow");
                currPosition = bundle.getInt("position", 0);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onLoaded();
        }

        private void onLoaded() {
            try {
                rlNext.setVisibility(articles.size() > 1 ? View.VISIBLE : View.GONE);
                rlPrev.setVisibility(articles.size() > 1 ? View.VISIBLE : View.GONE);
                int actionbar_height = Preferences.getInt(getApplicationContext(), ApiListEnums.ACTIONBAR_HEIGHT.getType(), 100);
                ColumnistPaperAdapter columnistPaperAdapter = new ColumnistPaperAdapter(AllColumnistsActivity.this, articles, allArticleBtnIsShow, actionbar_height);
                columnistPaperAdapter.setScrllListener(AllColumnistsActivity.this);
                viewPager.setAdapter(columnistPaperAdapter);
                viewPager.setOffscreenPageLimit(1);
                circleIndicator.setViewPager(viewPager);
                viewPager.setCurrentItem(currPosition, true);
                RLPageIndicator.setVisibility(articles.size() > 1 ? View.VISIBLE : View.GONE);
                DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(getApplicationContext(), ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
                llLoading.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                new TurkuvazAnalytic(AllColumnistsActivity.this).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.COLUMNIST_DETAILS.getEventName()).sendEvent();
                onScrll(false, "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onScrll(boolean isVisible, String title) {
        txtTitle.setText(title);
        toolbar.setBackgroundColor(Color.parseColor(Preferences.getString(getApplicationContext(), ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF")));
        toolbar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        int styleId = Preferences.getString(getApplicationContext(), ApiListEnums.THEME.getType(), "dark").equals("dark")
                ? R.style.TextAppearance_ColumnistCollapseTitleDark : R.style.TextAppearance_ColumnistCollapseTitleLight;
        if (Build.VERSION.SDK_INT < 23) {
            txtTitle.setTextAppearance(getApplicationContext(), styleId);
        } else {
            txtTitle.setTextAppearance(styleId);
        }
    }

    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    @Override
    public void onPopupStart(String videoUrl) {
        try {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }

            TypedValue tv = new TypedValue();
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
            if (resourceId > 0) {
                actionBarHeight += getResources().getDimensionPixelSize(resourceId);
            }

            if (GlobalMethods.isActiveNativePlayer()) {
                LayoutInflater layoutInflater = LayoutInflater.from(AllColumnistsActivity.this);
                popupView = layoutInflater.inflate(R.layout.popup_nativ_player, null);
                popupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                NativeMediaPlayer nativeMediaPlayer = popupView.findViewById(R.id.nativeMediaPlayerPopup);
                popupView.findViewById(R.id.btn_sparkPopupClose).setOnClickListener(view -> popupWindow.dismiss());
                nativeMediaPlayer.setActivity(AllColumnistsActivity.this);

                nativeMediaPlayer.setVideoURL(videoUrl);
                nativeMediaPlayer.init();

                nativeMediaPlayer.setOnFullScreenChange(isFullScreen -> {
                    try {
                        if (isFullScreen) {
                            AllColumnistsActivity.this.getSupportActionBar().hide();
                            popupView.findViewById(R.id.frameToolbar).setVisibility(View.GONE);
                            popupView.setBackgroundColor(Color.WHITE);
                        } else {
                            AllColumnistsActivity.this.getSupportActionBar().show();
                            popupView.findViewById(R.id.frameToolbar).setVisibility(View.VISIBLE);
                            popupView.setBackgroundColor(Color.TRANSPARENT);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                popupView.setOnTouchListener((view, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            pressStartTime = System.currentTimeMillis();
                            pressedX = (int) event.getX();
                            pressedY = (int) event.getY();
                            break;
                        }

                        case MotionEvent.ACTION_UP: {
                            long pressDuration = System.currentTimeMillis() - pressStartTime;
                            if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(AllColumnistsActivity.this, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
                                // Click Action
                            }
                            break;
                        }

                        case MotionEvent.ACTION_MOVE:
                            offsetX = (int) event.getRawX() - pressedX;
                            offsetY = (int) event.getRawY() - pressedY;
                            if (offsetY > actionBarHeight)
                                popupWindow.update(offsetX, offsetY, -1, -1, true);
                            break;
                    }
                    return true;
                });

            } else {
                LayoutInflater layoutInflater = LayoutInflater.from(AllColumnistsActivity.this);
                popupView = layoutInflater.inflate(R.layout.popup, null);
                popupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                sparkPlayerViewPopup = popupView.findViewById(R.id.sparkplayer_popup);
                sparkPlayerViewPopup.setVideoURL(videoUrl);
                sparkPlayerViewPopup.setOnClosePopupListener(() -> popupWindow.dismiss());
                sparkPlayerViewPopup.init();

                ((ViewGroup) sparkPlayerViewPopup.getChildAt(0)).getChildAt(0).setOnTouchListener(this);
                popupWindow.setOnDismissListener(() -> {
                    sparkPlayerViewPopup.closeSpark();
                    popupWindow = null;
                    popupView = null;
                    sparkPlayerViewPopup = null;
                });
            }

            if (popupWindow != null)
                popupWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, actionBarHeight);

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                pressStartTime = System.currentTimeMillis();
                pressedX = (int) event.getX();
                pressedY = (int) event.getY();
                break;
            }

            case MotionEvent.ACTION_UP: {
                long pressDuration = System.currentTimeMillis() - pressStartTime;
                if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(AllColumnistsActivity.this, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
                    // Click Action
                }
                break;
            }

            case MotionEvent.ACTION_MOVE:
                offsetX = (int) event.getRawX() - pressedX;
                offsetY = (int) event.getRawY() - pressedY;
                if (offsetY > actionBarHeight)
                    popupWindow.update(offsetX, offsetY, -1, -1, true);
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        if (popupView != null) {
            if (sparkPlayerViewPopup != null)
                sparkPlayerViewPopup.closeSpark();
            popupWindow.dismiss();
            popupWindow = null;
            popupView = null;
            sparkPlayerViewPopup = null;
        }
        super.onPause();
    }
}
