package turkuvaz.sdk.models.Models.StreamingList;

public class DaysModel {

    private String dayName;
    private String dayNumber;
    private String monthName;

    public DaysModel(String dayName, String dayNumber, String monthName) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
        this.monthName = monthName;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.dayName.equals(((DaysModel) obj).getDayName())) {
            return true;
        }

        return false;
    }
}
