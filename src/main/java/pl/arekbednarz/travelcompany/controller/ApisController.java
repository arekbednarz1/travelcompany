package pl.arekbednarz.travelcompany.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.arekbednarz.travelcompany.model.JSON.TravelTimeBusJSON;
import pl.arekbednarz.travelcompany.model.JSON.TravelTimeWalkandBikeAndShortestBusJSON;
import pl.arekbednarz.travelcompany.model.speedApiModel.Travel;
import pl.arekbednarz.travelcompany.service.impl.ApiServiceImpl;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.*;

@Controller
public class ApisController {

    private final ApiServiceImpl apiService;

    public ApisController(ApiServiceImpl apiService) {
        this.apiService = apiService;
    }


    @GetMapping("/time/walk/{source}/{destination}")
    @ResponseBody
    ResponseEntity<TravelTimeWalkandBikeAndShortestBusJSON> getTravelWalk(@PathVariable String source, @PathVariable String destination) throws IOException, IndexOutOfBoundsException, IllegalArgumentException {
        int km = apiService.getDistanceByWalk(source, destination);
        int travelInMinuts = (km / 6) * 60;
        return ResponseEntity.ok(new TravelTimeWalkandBikeAndShortestBusJSON(travelInMinuts));

    }

    @GetMapping("/time/bus/{source}/{destination}")
    @ResponseBody
    ResponseEntity<List<TravelTimeBusJSON>> getTravelByBus(@PathVariable String source, @PathVariable String destination) throws IOException {
        TravelTimeBusJSON json = new TravelTimeBusJSON();
        Map<Long, Travel> arriveMap = apiService.getDistanceByBus(source, destination);
        List<TravelTimeBusJSON> requestList = new ArrayList<>();
        Iterator<Map.Entry<Long, Travel>> iterator = arriveMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Travel> pair = iterator.next();
            json.setDeparture(pair.getValue().getDepartureTime());
            json.setTime(pair.getKey());
            requestList.add(json);

        }
        return ResponseEntity.ok(requestList);

    }

    @GetMapping("/time/bike/{source}/{destination}/{day}")
    @ResponseBody
    ResponseEntity<TravelTimeWalkandBikeAndShortestBusJSON> getTravelByBike(@PathVariable String source, @PathVariable String destination, @PathVariable String day) throws IllegalArgumentException, IOException {
        int distance = apiService.getDistanceByBike(source, destination, day);

        return ResponseEntity.ok(new TravelTimeWalkandBikeAndShortestBusJSON(distance));
    }


    @GetMapping("/travel/bus/{source}/{destination}/{day}/{time}")
    @ResponseBody
    ResponseEntity<TravelTimeWalkandBikeAndShortestBusJSON> getShortestTravelByBus(@PathVariable String source, @PathVariable String destination, @PathVariable String day, @PathVariable LocalTime time) throws IOException {
//        the day is only needed when I'm looking for the weather
        int timeAll = 0;
        List<Long> timeList = apiService.shortestDistanceByBus(source, destination, day, time);
        for (int i = 0; i < timeList.size() - 1; i++) {
            timeAll += timeList.get(i);
        }
        TravelTimeWalkandBikeAndShortestBusJSON json = new TravelTimeWalkandBikeAndShortestBusJSON(timeAll);
        return ResponseEntity.ok(json);

    }


    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(IOException.class)
    ResponseEntity<String> handleIo(IOException e) {
        return ResponseEntity.badRequest().body(e.getMessage());

    }

    @ExceptionHandler(DateTimeException.class)
    ResponseEntity<String> handleDate(DateTimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());

    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    ResponseEntity<String> indexOutOfBoundException(IndexOutOfBoundsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());

    }


}


