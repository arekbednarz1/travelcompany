package pl.arekbednarz.travelcompany.service.impl;

import org.springframework.stereotype.Service;
import pl.arekbednarz.travelcompany.RestClient;
import pl.arekbednarz.travelcompany.model.geoApiModel.Distance;
import pl.arekbednarz.travelcompany.model.speedApiModel.Timetable;
import pl.arekbednarz.travelcompany.model.speedApiModel.Travel;
import pl.arekbednarz.travelcompany.model.sunApiModel.City;
import pl.arekbednarz.travelcompany.model.sunApiModel.Day;
import pl.arekbednarz.travelcompany.model.sunApiModel.Weather;
import pl.arekbednarz.travelcompany.service.ApiService;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApiServiceImpl implements ApiService {

    private final RestClient restClient;

    public ApiServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    public Integer getDistanceByWalk(String source, String destination) throws IOException, IllegalArgumentException, IndexOutOfBoundsException {
        List<Distance> travelList = restClient.getGeoApi().getDistances().stream()
                .filter(distance -> distance.getSource().equals(source))
                .filter(distance -> distance.getDestination().equals(destination))
                .collect(Collectors.toList());
        if (travelList.size()==1){

            return travelList.get(0).getDistance().intValue();
        }
        List<Long> kmToDestinationList = restClient.getGeoApi().getDistances().stream().filter(d -> d.getDestination().equals(destination) || d.getSource().equals(destination) ).map(Distance::getDistance).collect(Collectors.toList());
        long min=0;
        try {
            min = kmToDestinationList.get(0);
        }catch (IndexOutOfBoundsException e){
            throw new IndexOutOfBoundsException("City not found");
        }
        for (int i = 0; i < kmToDestinationList.size()-1 ; i++) {
            if (min>kmToDestinationList.get(i))
                min = Math.toIntExact(kmToDestinationList.get(i));
            }

        int km =0;
        List<Integer>toCHeck = new ArrayList<>();
        long finalMin = min;
        List<Distance> cityStop = restClient.getGeoApi().getDistances().stream().filter(d -> d.getDestination().equals(destination)|| d.getSource().equals(destination) ).filter(k -> k.getDistance().equals(finalMin)).collect(Collectors.toList());
        String cityS=cityStop.get(0).getDestination();
        if (cityS.equals(destination)){
            cityS=cityStop.get(0).getSource();
        }
        km+=cityStop.get(0).getDistance();
            String finalCityS = cityS;
            List<Distance> cityStart = restClient.getGeoApi().getDistances().stream().filter(d -> d.getDestination().equals(source)|| d.getSource().equals(source) ).filter(k->k.getDestination().equals(finalCityS) || k.getSource().equals(finalCityS)).collect(Collectors.toList());
           km+=cityStart.get(0).getDistance();
//            List<Long> kmToStop = restClient.getGeoApi().getDistances().stream().filter(d -> d.getDestination().equals(city) && d.getSource().equals(source)).map(Distance::getDistance).collect(Collectors.toList());
            toCHeck.add(km);
           if (toCHeck.size()==1) {
               return km;
           }
        throw new IOException("Cities not found");

    }


    @Override
    public Map<Long, Travel> getDistanceByBus(String source, String destination) throws IOException, IllegalArgumentException, DateTimeException {

        Map<Long, Travel> arriveTime = new HashMap<>();

        List<List<Travel>> travelList = restClient.getSpeedApi().getTimetable().stream().filter(d -> d.getSource().equals(source) && d.getDestination().equals(destination))
                .map(Timetable::getTravels)
                .collect(Collectors.toList());

        for (List<Travel> t : travelList) {
            try {
            for (Travel bus : t) {
                Duration duration = Duration.between(LocalTime.parse(bus.getDepartureTime()), LocalTime.parse(bus.getDestinationTime()));
                arriveTime.put(duration.toMinutes(), bus);
            }

            } catch (DateTimeException e) {
                throw new DateTimeException("Invalid Date format in api");
            }
            if (arriveTime.size() > 0) {
                return arriveTime;

            }

        }throw new IllegalArgumentException("bus between 2 city not found");
    }

    @Override
    public Integer getDistanceByBike(String source, String destination, String day) throws IllegalArgumentException, IOException {

        int distance = getDistanceByWalk(source, destination);


        List<Weather> weatherToCheck = new ArrayList<>();
        List<Day> weatherinDay = restClient.getSunApi().getDays().stream()
                .filter(d -> d.getDay().equals(day.toLowerCase()))
                .collect(Collectors.toList());
        List<List<City>> weatherInCity = weatherinDay.stream()
                .map(Day::getCities)
                .collect(Collectors.toList());
        for (List<City> ct : weatherInCity) {
            for (City c : ct) {
                if (c.getCity().equals(source) || c.getCity().equals(destination)) {
                    weatherToCheck.add(c.getWeather());

                }
            }
        }
        if (((weatherToCheck.get(0).getTemperature() >= 10 && weatherToCheck.get(0).getTemperature() <= 25) && weatherToCheck.get(0).getWind() < 70
                && weatherToCheck.get(0).getProbabilityOfPrecipitation() < 50) && ((weatherToCheck.get(1).getTemperature() >= 10 && weatherToCheck.get(1).getTemperature() <= 25)
                && weatherToCheck.get(1).getWind() < 70 && weatherToCheck.get(1).getProbabilityOfPrecipitation() < 50)) {
            return distance;
        }
        throw new IllegalArgumentException("travel impossible bad weather");
    }


    @Override
    public List<Long> shortestDistanceByBus(String source, String destination, String day, LocalTime time) throws DateTimeException, IOException {


        List<Long> returnList = new ArrayList<>();
        int busStart = 0;
        int hour =  time.getHour();
        Map<Long, Travel> travel = new HashMap<>();

        try {
            travel=getDistanceByBus(source, destination);
            for (Map.Entry<Long, Travel> pair : travel.entrySet()) {
                returnList.add(pair.getKey());
                return returnList;
            }
        }catch (IllegalArgumentException | IOException e){
            Map<Long, Travel> arriveTime = new HashMap<>();
            List<List<Travel>> travelList = new ArrayList<>();

            travelList = restClient.getSpeedApi().getTimetable().stream().filter(t -> t.getDestination().equals(destination))
                    .map(Timetable::getTravels)
                    .collect(Collectors.toList());
            for (List<Travel> t : travelList) {

                for (Travel bus : t) {
                    try {
                        busStart = LocalTime.parse(bus.getDepartureTime()).getHour();
                        if ((busStart - hour) <= 1 || (hour - busStart) <= 1) {
                            Duration duration = Duration.between(LocalTime.parse(bus.getDepartureTime()), LocalTime.parse(bus.getDestinationTime()));
                            arriveTime.put(duration.toMinutes(), bus);

                        }
                    } catch (DateTimeException ex) {
                        throw new DateTimeException("Invalid Date format in api");
                    }
                }
            }
            travelList = restClient.getSpeedApi().getTimetable().stream().filter(t -> t.getDestination().equals(destination))
                    .filter(t -> t.getDestination().equals(source)).map(Timetable::getTravels).collect(Collectors.toList());
            for (List<Travel> t : travelList) {
                for (Travel bus : t) {
                    try {

                        busStart = LocalTime.parse(bus.getDepartureTime()).getHour();
                        if ((busStart - hour) <= 1 || (hour - busStart) <= 1) {
                            Duration duration = Duration.between(LocalTime.parse(bus.getDepartureTime()), LocalTime.parse(bus.getDestinationTime()));
                            arriveTime.put(duration.toMinutes(), bus);
                        }
                    } catch (DateTimeException ex) {
                        throw new DateTimeException("Invalid Date format in api");
                    }
                }
                if (arriveTime.size() > 0) {
                    Iterator<Map.Entry<Long, Travel>> iterator = arriveTime.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Long, Travel> pair = iterator.next();
                        returnList.add(pair.getKey());
                        return returnList;
                    }
                } else if (arriveTime.size() == 0) {
                    List<Timetable> firstToSecondCityTimetable = restClient.getSpeedApi().getTimetable().stream().filter(p -> p.getDestination().equals(source))
                            .collect(Collectors.toList());
                    List<Timetable> thirdToLastTimetable = restClient.getSpeedApi().getTimetable().stream().filter(s -> s.getDestination().equals(destination))
                            .collect(Collectors.toList());
                    List<Timetable> secondToThirdCityTimetable = new ArrayList<>();

                    String firstCity = source;
                    String secondCity = "";
                    String thirdCity = "";
                    String fourthCity = destination;

                    for (int i = 0; i < thirdToLastTimetable.size() - 1; i++) {
                        Timetable city = thirdToLastTimetable.get(i);
                        String thirdCitiesFromList = thirdToLastTimetable.get(i).getSource();
                        List<String> secondCitiesList = firstToSecondCityTimetable.stream().map(Timetable::getSource).collect(Collectors.toList());

                        if (secondCitiesList.contains(thirdCitiesFromList)) {
                            thirdCity = thirdCitiesFromList;
                            String finalThirdCity = thirdCity;
                            secondToThirdCityTimetable = restClient.getSpeedApi().getTimetable().stream().filter(timetable -> timetable.getDestination().equals(finalThirdCity)).collect(Collectors.toList());
                            secondCity = secondToThirdCityTimetable.stream().map(c -> c.getSource()).toString();
                        }
                    }
                    if (firstCity == "" || secondCity == "" || thirdCity == "" || fourthCity == "") {
                        throw new IOException("Invalid city");
                    }

                    String finalSecondCity = secondCity;
                    firstToSecondCityTimetable = restClient.getSpeedApi().getTimetable().stream().filter(n -> n.getSource().equals(firstCity) && n.getDestination().equals(finalSecondCity)).collect(Collectors.toList());
                    String finalThirdCity1 = thirdCity;
                    secondToThirdCityTimetable = restClient.getSpeedApi().getTimetable().stream().filter(n -> n.getSource().equals(finalSecondCity) && n.getDestination().equals(finalThirdCity1)).collect(Collectors.toList());
                    thirdToLastTimetable = restClient.getSpeedApi().getTimetable().stream().filter(n -> n.getSource().equals(finalThirdCity1) && n.getDestination().equals(fourthCity)).collect(Collectors.toList());

                    for (Timetable k : firstToSecondCityTimetable) {
                        for (Travel tr : k.getTravels()) {
                            busStart = LocalTime.parse(tr.getDepartureTime()).getHour();
                            if ((busStart - hour) <= 1 || (hour - busStart) <= 1) {
                                Duration duration = Duration.between(LocalTime.parse(tr.getDepartureTime()), LocalTime.parse(tr.getDestinationTime()));
                                long firstToSecondTime = duration.toMinutes();
                                returnList.add(firstToSecondTime);

                            }
                        }
                    }

                    eachTimeTravel(returnList, secondToThirdCityTimetable);
                    eachTimeTravel(returnList, thirdToLastTimetable);


                }
            }

            return returnList;
        } throw new IOException("Invalid city");


    }


    private void eachTimeTravel(List<Long> returnList, List<Timetable> timetable) {
        int busStart;
        for (Timetable t : timetable) {
            for (Travel tr : t.getTravels()) {
                busStart = LocalTime.parse(tr.getDepartureTime()).getHour();
                Duration duration = Duration.between(LocalTime.parse(tr.getDepartureTime()), LocalTime.parse(tr.getDestinationTime()));
                long thirdToFourth = duration.toMinutes();
                returnList.add(thirdToFourth);

            }
        }
    }
}









