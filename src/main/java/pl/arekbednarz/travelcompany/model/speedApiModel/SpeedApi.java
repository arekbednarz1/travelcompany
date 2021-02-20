
package pl.arekbednarz.travelcompany.model.speedApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpeedApi {

    @SerializedName("timetable")
    @Expose
    private List<Timetable> timetable = null;

    public List<Timetable> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<Timetable> timetable) {
        this.timetable = timetable;
    }

    @Override
    public String toString() {
        return "SpeedApi{" +
                "timetable=" + timetable +
                '}';
    }
}
