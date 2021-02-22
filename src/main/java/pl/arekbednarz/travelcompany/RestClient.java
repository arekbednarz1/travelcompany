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


        }

