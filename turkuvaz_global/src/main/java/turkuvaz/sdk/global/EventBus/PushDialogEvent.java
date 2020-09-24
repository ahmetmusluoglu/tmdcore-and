package turkuvaz.sdk.global.EventBus;

import turkuvaz.sdk.global.FirebasePush.PushModel;

/**
 * Created by turgay.ulutas on 09/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class PushDialogEvent {
    private PushModel pushModel;

    public PushDialogEvent(PushModel pushModel) {
        this.pushModel = pushModel;
    }

    public PushModel getPushModel() {
        return pushModel;
    }

    public void setPushModel(PushModel pushModel) {
        this.pushModel = pushModel;
    }
}
