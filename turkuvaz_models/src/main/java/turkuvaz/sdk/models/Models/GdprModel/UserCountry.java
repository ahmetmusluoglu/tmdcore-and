package turkuvaz.sdk.models.Models.GdprModel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "UserCountry")
public class UserCountry {

    @Element(name = "CountryCode")
    private String CountryCode;

    @Element(name = "CountryName")
    private String CountryName;

    @Element(name = "IP")
    private String IP;

    public UserCountry() {
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
