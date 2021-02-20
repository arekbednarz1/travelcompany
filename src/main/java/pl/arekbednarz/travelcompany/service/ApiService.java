package pl.arekbednarz.travelcompany.service;

import pl.arekbednarz.travelcompany.model.speedApiModel.Travel;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface ApiService {

   Integer getDistanceByWalk(String source, String destination) throws IOException;
   Map<Long, Travel> getDistanceByBus(String source, String destination) throws IOException;
   Integer getDistanceByBike(String source, String destination, String day) throws IOException;
   List<Long> shortestDistanceByBus(String source, String destination, String day, LocalTime time) throws IOException, DateTimeException;



}
