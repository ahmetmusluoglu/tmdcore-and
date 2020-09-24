package turkuvaz.sdk.global.ErrorDialog;

/**
 * Created by Ahmet MUŞLUOĞLU on 3/17/2020.
 */

public class ErrorLogModel {
    public String userToken;
    public String appVersion;
    public String pushUrl;
    public String pushRefID;
    public String pushTitle;
    public String errorBody;
    public String errorUrl;
    public int phoneOS;
    public String userDescription;
    public String deviceModel;
    public String screen;

    public ErrorLogModel(String userToken, String appVersion, String pushUrl, String pushRefID, String pushTitle, String errorBody, String errorUrl, int phoneOS, String deviceModel, String userDescription, String screen) {
        this.userToken = userToken;
        this.appVersion = appVersion;
        this.pushUrl = pushUrl;
        this.pushRefID = pushRefID;
        this.pushTitle = pushTitle;
        this.errorBody = errorBody;
        this.errorUrl = errorUrl;
        this.phoneOS = phoneOS;
        this.userDescription = userDescription;
        this.screen = screen;
        this.deviceModel = deviceModel;
    }

    public ErrorLogModel(String userToken, String appVersion, String errorBody, String errorUrl, int phoneOS, String deviceModel, String userDescription, String screen) {
        this.userToken = userToken;
        this.appVersion = appVersion;
        this.errorBody = errorBody;
        this.errorUrl = errorUrl;
        this.phoneOS = phoneOS;
        this.screen = screen;
        this.userDescription = userDescription;
        this.deviceModel = deviceModel;
    }

    public ErrorLogModel(String userToken, String appVersion, String pushUrl, String pushRefID, String pushTitle, String errorBody, String errorUrl, int phoneOS, String deviceModel, String screen) {
        this.userToken = userToken;
        this.appVersion = appVersion;
        this.pushUrl = pushUrl;
        this.pushRefID = pushRefID;
        this.pushTitle = pushTitle;
        this.errorBody = errorBody;
        this.errorUrl = errorUrl;
        this.phoneOS = phoneOS;
        this.screen = screen;
        this.deviceModel = deviceModel;
    }

    public ErrorLogModel(String userToken, String appVersion, String errorBody, String errorUrl, int phoneOS, String deviceModel, String screen) {
        this.userToken = userToken;
        this.appVersion = appVersion;
        this.errorBody = errorBody;
        this.errorUrl = errorUrl;
        this.phoneOS = phoneOS;
        this.screen = screen;
        this.deviceModel = deviceModel;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public String getPushRefID() {
        return pushRefID;
    }

    public void setPushRefID(String pushRefID) {
        this.pushRefID = pushRefID;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(String errorBody) {
        this.errorBody = errorBody;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public int getPhoneOS() {
        return phoneOS;
    }

    public void setPhoneOS(int phoneOS) {
        this.phoneOS = phoneOS;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
}