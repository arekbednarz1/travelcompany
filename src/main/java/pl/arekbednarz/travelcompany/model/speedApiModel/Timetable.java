
package pl.arekbednarz.travelcompany.model.speedApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Timetable {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("travels")
    @Expose
    private List<Travel> travels = null;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<Travel> getTravels() {
        return travels;
    }

    public void setTravels(List<Travel> travels) {
        this.travels = travels;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", travels=" + travels +
                '}';
    }
}
