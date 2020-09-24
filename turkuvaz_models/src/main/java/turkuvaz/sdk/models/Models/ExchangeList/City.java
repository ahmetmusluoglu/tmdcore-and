
package turkuvaz.sdk.models.Models.ExchangeList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("Enlem")
    @Expose
    private String enlem;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("NameForUrl")
    @Expose
    private String nameForUrl;
    @SerializedName("Boylam")
    @Expose
    private String boylam;
    @SerializedName("PlateNo")
    @Expose
    private Integer plateNo;

    public String getEnlem() {
        return enlem;
    }

    public void setEnlem(String enlem) {
        this.enlem = enlem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameForUrl() {
        return nameForUrl;
    }

    public void setNameForUrl(String nameForUrl) {
        this.nameForUrl = nameForUrl;
    }

    public String getBoylam() {
        return boylam;
    }

    public void setBoylam(String boylam) {
        this.boylam = boylam;
    }

    public Integer getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(Integer plateNo) {
        this.plateNo = plateNo;
    }

}
