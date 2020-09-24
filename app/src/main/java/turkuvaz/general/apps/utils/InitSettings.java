package turkuvaz.general.apps.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

import turkuvaz.general.apps.R;
import turkuvaz.sdk.models.Models.Preferences;

/**
 * Created by Ahmet MUŞLUOĞLU on 1/29/2020.
 */

@SuppressLint("ResourceType")
public class InitSettings {

    public InitSettings(Context context) {
        /**string values...*/
        this.configId = "0";
        this.toolbarBackColor = context.getResources().getString(R.color.colorPrimary);
        this.toolbarIconsColor = context.getResources().getString(R.color.beyaz);
        this.menuHeaderColor = "";

        /**boolean values...*/
        this.appTheme = "dark";
        this.splashOnlyImage = false;
        this.showBottomNavigationView = false;
        this.showBottomNavigationViewCustom = false;

        /**integer values...*/
        this.minikaImageResId = 0;
        this.splashImageResId = 0;
        this.menuSocialBarColor = 0;
        this.splashBackColor = context.getResources().getColor(R.color.colorPrimary);
        this.menuListBackColor = context.getResources().getColor(R.color.colorPrimary);
        this.menuListTextColor = context.getResources().getColor(R.color.siyah);
    }

    /**
     * string values...
     */
    private String configId;
    private String toolbarBackColor;
    private String toolbarIconsColor;
    private String menuHeaderColor;

    /**
     * boolean values...
     */
    private String appTheme;
    private boolean splashOnlyImage;
    private boolean showBottomNavigationView;
    private boolean showBottomNavigationViewCustom;
    private boolean needRadio;

    /**
     * integer values...
     */
    private int minikaImageResId;
    private int splashImageResId;
    private int splashBackColor;
    private int menuListBackColor;
    private int menuListTextColor;
    private int menuSocialBarColor;


    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getToolbarBackColor() {
        return toolbarBackColor;
    }

    public void setToolbarBackColor(String toolbarBackColor) {
        this.toolbarBackColor = toolbarBackColor;
    }

    public String getToolbarIconsColor() {
        return toolbarIconsColor;
    }

    public void setToolbarIconsColor(String toolbarIconsColor) {
        this.toolbarIconsColor = toolbarIconsColor;
    }

    public String getMenuHeaderColor() {
        return menuHeaderColor;
    }

    public void setMenuHeaderColor(String menuHeaderColor) {
        this.menuHeaderColor = menuHeaderColor;
    }

    public int getMenuSocialBarColor() {
        return menuSocialBarColor;
    }

    public void setMenuSocialBarColor(int menuSocialBarColor) {
        this.menuSocialBarColor = menuSocialBarColor;
    }

    public String getAppTheme() {
        return appTheme;
    }

    public void setAppTheme(String appTheme) {
        this.appTheme = appTheme;
    }

    public boolean isSplashOnlyImage() {
        return splashOnlyImage;
    }

    public void setSplashOnlyImage(boolean splashOnlyImage) {
        this.splashOnlyImage = splashOnlyImage;
    }

    public boolean isShowBottomNavigationView() {
        return showBottomNavigationView;
    }

    public void setShowBottomNavigationView(boolean showBottomNavigationView) {
        this.showBottomNavigationView = showBottomNavigationView;
    }

    public boolean isShowBottomNavigationViewCustom() {
        return showBottomNavigationViewCustom;
    }

    public void setShowBottomNavigationViewCustom(boolean showBottomNavigationViewCustom) {
        this.showBottomNavigationViewCustom = showBottomNavigationViewCustom;
    }

    public int getSplashImageResId() {
        return splashImageResId;
    }

    public void setSplashImageResId(int splashImageResId) {
        this.splashImageResId = splashImageResId;
    }

    public int getSplashBackColor() {
        return splashBackColor;
    }

    public void setSplashBackColor(int splashBackColor) {
        this.splashBackColor = splashBackColor;
    }

    public int getMenuListBackColor() {
        return menuListBackColor;
    }

    public void setMenuListBackColor(int menuListBackColor) {
        this.menuListBackColor = menuListBackColor;
    }

    public int getMenuListTextColor() {
        return menuListTextColor;
    }

    public void setMenuListTextColor(int menuListTextColor) {
        this.menuListTextColor = menuListTextColor;
    }

    public boolean isNeedRadio() {
        return needRadio;
    }

    public void setNeedRadio(boolean needRadio) {
        this.needRadio = needRadio;
    }

    public void setPid(Context context, int pidPhone, int pidTablet){
        // sol menüde bize ulaşın menüsünde gerekiyor. şuan sadece a news te kullanılacak
        Preferences.save(context, "pidIdPhone", pidPhone);
        Preferences.save(context, "pidIdTablet", pidTablet);
    }

    public void setMinikaImageResId(int minikaImageResId) {
        this.minikaImageResId = minikaImageResId;
    }

    public int getMinikaImageResId() {
        return minikaImageResId;
    }
}
