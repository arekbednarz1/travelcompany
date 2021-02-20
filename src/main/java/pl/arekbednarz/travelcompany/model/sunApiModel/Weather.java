
package pl.arekbednarz.travelcompany.model.sunApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("wind")
    @Expose
    private Long wind;
    @SerializedName("temperature")
    @Expose
    private Long temperature;
    @SerializedName("probabilityOfPrecipitation")
    @Expose
    private Long probabilityOfPrecipitation;

    public Long getWind() {
        return wind;
    }

    public void setWind(Long wind) {
        this.wind = wind;
    }

    public Long getTemperature() {
        return temperature;
    }

    public void setTemperature(Long temperature) {
        this.temperature = temperature;
    }

    public Long getProbabilityOfPrecipitation() {
        return probabilityOfPrecipitation;
    }

    public void setProbabilityOfPrecipitation(Long probabilityOfPrecipitation) {
        this.probabilityOfPrecipitation = probabilityOfPrecipitation;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "wind=" + wind +
                ", temperature=" + temperature +
                ", probabilityOfPrecipitation=" + probabilityOfPrecipitation +
                '}';
    }
}
