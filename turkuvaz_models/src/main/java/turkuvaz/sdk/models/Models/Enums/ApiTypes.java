package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 04/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public enum ApiTypes {
//    LIVE("https://api.tmgrup.com.tr"),
//    QA("http://api-qa.dev.tmd"),
//    TEST("http://api-test.dev.tmd"),
    IP_CHECK("https://ipcheck.tmgrup.com.tr"); // bununla ilgili bir çalışma yapılmadı. Diğer 3 link sharedpref'e atıldı ve ayarlar ekranındaki versiyon metnine 10 defa tıklandığında linkler değişecek

    private String type;

    ApiTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
