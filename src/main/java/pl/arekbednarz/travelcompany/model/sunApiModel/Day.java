
package pl.arekbednarz.travelcompany.model.sunApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Day {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("cities")
    @Expose
    private List<City> cities = null;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "Day{" +
                "day='" + day + '\'' +
                ", cities=" + cities +
                '}';
    }
}
