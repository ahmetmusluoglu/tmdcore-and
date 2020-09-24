package turkuvaz.general.apps.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import turkuvaz.general.apps.R;
import turkuvaz.sdk.global.EventBus.PushDialogEvent;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.Preferences;

public abstract class CoreBaseActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    protected BottomNavigationView navigationView;
    protected RelativeLayout rlBottomNavigationView;

    protected abstract int getContentViewId();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        Preferences.save(this, "isOpenCustomPage", false);
        IconTypes.resetIcons();

        CoreApp.getInstance().generalControls();

        /** GENERAL VIEWS CONTROL */
        configurationViews();

    }

    private void configurationViews() {
        navigationView = findViewById(R.id.bottom_nav_view);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationViewHome = findViewById(R.id.nav_view);
        if (navigationViewHome != null) {
            LinearLayout ly_headerBackHome = navigationViewHome.getHeaderView(0).findViewById(R.id.ly_headerBackHome);
            TextView tvLeftMenuTitle = navigationViewHome.getHeaderView(0).findViewById(R.id.textView);
            if (ly_headerBackHome != null) { // sol menü üst banner
                ly_headerBackHome.setBackgroundColor(Color.parseColor(Preferences.getString(CoreBaseActivity.this, ApiListEnums.LEFT_MENU_TOP_BACK.getType(), "#FFFFFF"))); // sol menü banner arkaplan rengi
                ly_headerBackHome.setOnClickListener(view -> goHome());
            }
            if (tvLeftMenuTitle != null) // sol menü banner başlık rengi
                tvLeftMenuTitle.setTextColor(Color.parseColor(Preferences.getString(CoreBaseActivity.this, ApiListEnums.LEFT_MENU_TOP_ICON.getType(), "#FFFFFF")));
        }

        rlBottomNavigationView = findViewById(R.id.rlBottomNavigationView);
        rlBottomNavigationView.setVisibility(View.GONE);

    }

    private void setLeftMenu() {
    }

    public void goHome() {
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        startActivity(new Intent(this, CoreHomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PushDialogEvent event) {

    }

    public void clickToolbarLogo(View view) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        Preferences.save(this, "appIsOpen", true);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            Preferences.save(this, "appIsOpen", true);
            if (Preferences.getBoolean(this, "isOpenCustomPage", false)) {
                Preferences.save(this, "isOpenCustomPage", false);
                IconTypes.resetIcons();
            }
            Preferences.removeCustomKeys(CoreBaseActivity.this); // custom sayfalardan geri dönüş yapıldığında, custom veriler silinmeli. yoksa renkler karışıyor.
            setLeftMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Preferences.save(this, "appIsOpen", false);
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        Preferences.save(this, "appIsOpen", false);
        super.onDestroy();
    }
}
