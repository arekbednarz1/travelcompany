
package pl.arekbednarz.travelcompany.model.geoApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoApi {

    @SerializedName("distances")
    @Expose
    private List<Distance> distances = null;

    public List<Distance> getDistances() {
        return distances;
    }

    public void setDistances(List<Distance> distances) {
        this.distances = distances;
    }

    @Override
    public String toString() {
        return "GeoApi{" +
                "distances=" + distances +
                '}';
    }
}
