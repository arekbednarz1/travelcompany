
package pl.arekbednarz.travelcompany.model.speedApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Travel {

    @SerializedName("departureTime")
    @Expose
    private String departureTime;
    @SerializedName("destinationTime")
    @Expose
    private String destinationTime;

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDestinationTime() {
        return destinationTime;
    }

    public void setDestinationTime(String destinationTime) {
        this.destinationTime = destinationTime;
    }

    @Override
    public String toString() {
        return "Travel{" +
                "departureTime='" + departureTime + '\'' +
                ", destinationTime='" + destinationTime + '\'' +
                '}';
    }
}
