package turkuvaz.sdk.models.Models.StreamingList;

import java.util.Date;

public class TvStreamModel {

    private String startDate;
    private String endDate;
    private String programName;

    public TvStreamModel(String startDate, String endDate, String programName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.programName = programName;
    }

    public TvStreamModel() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }
}
