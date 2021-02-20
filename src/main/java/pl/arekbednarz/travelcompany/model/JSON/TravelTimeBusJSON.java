package pl.arekbednarz.travelcompany.model.JSON;


import pl.arekbednarz.travelcompany.model.speedApiModel.Travel;

import java.util.List;

public class TravelTimeBusJSON {

    public Long time;

    public String departure;

    public TravelTimeBusJSON(Long time) {
        this.time = time;
    }

    public TravelTimeBusJSON(Long time, String departure) {
        this.time = time;
        this.departure = departure;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public TravelTimeBusJSON() {
    }

    @Override
    public String toString() {
        return "travel{" +
                "time=" + time +
                ", departure='" + departure + '\'' +
                '}';
    }
}
