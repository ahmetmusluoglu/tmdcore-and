package turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils;

/**
 * Created by Ahmet MUŞLUOĞLU on 4/8/2020.
 */
public enum ExchangeEnums {
    EURO("EURO"),
    DOLAR("DOLAR"),
    ALTIN("ALTIN"),
    BIST100("BIST100");

    private String type;

    ExchangeEnums(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}



