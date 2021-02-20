
package pl.arekbednarz.travelcompany.model.sunApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("weather")
    @Expose
    private Weather weather;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }


    @Override
    public String toString() {
        return "City{" +
                "city='" + city + '\'' +
                ", weather=" + weather +
                '}';
    }
}
