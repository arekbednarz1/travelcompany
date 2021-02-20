
package pl.arekbednarz.travelcompany.model.geoApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Distance {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("distance")
    @Expose
    private Long distance;

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

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }


    @Override
    public String toString() {
        return "Distance{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", distance=" + distance +
                '}';
    }
}
