
package pl.arekbednarz.travelcompany.model.sunApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SunApi {

    @SerializedName("days")
    @Expose
    private List<Day> days = null;

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "SunApi{" +
                "days=" + days +
                '}';
    }
}
