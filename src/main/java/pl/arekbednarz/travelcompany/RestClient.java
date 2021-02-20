package pl.arekbednarz.travelcompany;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.arekbednarz.travelcompany.model.geoApiModel.Distance;
import pl.arekbednarz.travelcompany.model.geoApiModel.GeoApi;
import pl.arekbednarz.travelcompany.model.speedApiModel.SpeedApi;
import pl.arekbednarz.travelcompany.model.speedApiModel.Timetable;
import pl.arekbednarz.travelcompany.model.speedApiModel.Travel;
import pl.arekbednarz.travelcompany.model.sunApiModel.City;
import pl.arekbednarz.travelcompany.model.sunApiModel.Day;
import pl.arekbednarz.travelcompany.model.sunApiModel.SunApi;
import pl.arekbednarz.travelcompany.model.sunApiModel.Weather;
import pl.arekbednarz.travelcompany.service.impl.ApiServiceImpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class RestClient {

    @Bean
    public SunApi getSunApi() throws IOException {
        URL urlSunApi = new URL("http://resources.codeconcept.pl/api/weather");
        InputStreamReader readerSunApi = new InputStreamReader(urlSunApi.openStream());
        return new Gson().fromJson(readerSunApi, SunApi.class);
    }

    @Bean
    public SpeedApi getSpeedApi() throws IOException {
        URL urlSpeedApi = new URL("http://resources.codeconcept.pl/api/timetable");
        InputStreamReader readerSpeedApi = new InputStreamReader(urlSpeedApi.openStream());
        return new Gson().fromJson(readerSpeedApi, SpeedApi.class);
    }
    @Bean
    public GeoApi getGeoApi() throws IOException {
        URL urlGeoApi = new URL("http://resources.codeconcept.pl/api/distance");
        InputStreamReader readerGeoApi = new InputStreamReader(urlGeoApi.openStream());
        return new Gson().fromJson(readerGeoApi, GeoApi.class);
    }

    public static void main(String[] args) throws IOException {
//        URL urlGeoApi = new URL("http://resources.codeconcept.pl/api/distance");
//        InputStreamReader readerGeoApi = new InputStreamReader(urlGeoApi.openStream());
//        GeoApi geoApi = new Gson().fromJson(readerGeoApi, GeoApi.class);
//
//
//        Optional<Distance> cityStop = geoApi.getDistances().stream()
//                .filter(distance -> distance.getDestination().equals("Warszawa"))
//                .min(Comparator.comparingDouble(Distance::getDistance));
//
//        List<Distance> travelfromFirstToStop = geoApi.getDistances().stream()
//                .filter(distance -> distance.getSource().equals("Krakow") && distance.getDestination().equals(cityStop.get().getSource()))
//                .collect(Collectors.toList());
//
//        List<Distance> travelFromStopToEnd = geoApi.getDistances().stream()
//                .filter(distance -> distance.getSource().equals(cityStop.get().getSource()) && distance.getDestination().equals("Warszawa"))
//                .collect(Collectors.toList());
//
//        int dist= 0;
//        for (Distance s :travelfromFirstToStop) {
//           dist+= s.getDistance();
//            for (Distance d:travelFromStopToEnd) {
//                dist+=d.getDistance();
//
//            }
//
//        }
//
//        URL urlSpeedApi = new URL("http://resources.codeconcept.pl/api/timetable");
//        InputStreamReader readerSpeedApi = new InputStreamReader(urlSpeedApi.openStream());
//        SpeedApi speedApi = new Gson().fromJson(readerSpeedApi, SpeedApi.class);
//
//
//        Map<Long,Travel> arriveTime = new HashMap<>();
//
//            List<List<Travel>> travelList= speedApi.getTimetable().stream().filter(d->d.getSource().equals("Krakow") && d.getDestination().equals("Poznan"))
//                    .map(Timetable::getTravels)
//                    .collect(Collectors.toList());
//
//        for (List<Travel> t : travelList){
//               for (Travel bus: t){
//                   Duration duration = Duration.between(LocalTime.parse(bus.getDepartureTime()),LocalTime.parse(bus.getDestinationTime()));
//                   arriveTime.put(duration.toMinutes(),bus);
//               }
//            System.out.println(arriveTime);
//        }


        }



        }

