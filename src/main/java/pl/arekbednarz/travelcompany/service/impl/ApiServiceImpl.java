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
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        if (travelList.size() == 1) {

            return travelList.get(0).getDistance().intValue();
        }
        List<Long> kmToDestinationList = restClient.getGeoApi().getDistances().stream().filter(d -> d.getDestination().equals(destination) || d.getSource().equals(destination)).map(Distance::getDistance).collect(Collectors.toList());
        long min = 0;
        try {
            min = kmToDestinationList.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("City not found");
        }
        for (int i = 0; i < kmToDestinationList.size(); i++) {
            if (min > kmToDestinationList.get(i))
                min = Math.toIntExact(kmToDestinationList.get(i));
        }

        int km = 0;
        List<Integer> toCHeck = new ArrayList<>();
        long finalMin = min;
        List<Distance> cityStop = restClient.getGeoApi().getDistances().stream().filter(d -> d.getDestination().equals(destination) || d.getSource().equals(destination)).filter(k -> k.getDistance().equals(finalMin)).collect(Collectors.toList());
        String cityS = cityStop.get(0).getDestination();
        if (cityS.equals(destination)) {
            cityS = cityStop.get(0).getSource();
        }
        km += cityStop.get(0).getDistance();
        String finalCityS = cityS;
        List<Distance> cityStart = restClient.getGeoApi().getDistances().stream().filter(d -> d.getDestination().equals(source) || d.getSource().equals(source)).filter(k -> k.getDestination().equals(finalCityS) || k.getSource().equals(finalCityS)).collect(Collectors.toList());
        km += cityStart.get(0).getDistance();
//            List<Long> kmToStop = restClient.getGeoApi().getDistances().stream().filter(d -> d.getDestination().equals(city) && d.getSource().equals(source)).map(Distance::getDistance).collect(Collectors.toList());
        toCHeck.add(km);
        if (toCHeck.size() == 1) {
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

        }
        throw new IllegalArgumentException("bus between 2 city not found");
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
        throw new IllegalArgumentException("travel impossible, bad weather or City not found");
    }


    @Override
    public List<Long> shortestDistanceByBus(String source, String destination, String day, LocalTime time) throws DateTimeException, IOException {
        List<Long> returnList = new ArrayList<>();
        Map<Long, Travel> travel = new HashMap<>();
        try {
            travel = getDistanceByBus(source, destination);
            for (Map.Entry<Long, Travel> pair : travel.entrySet()) {
                returnList.add(pair.getKey());
                return returnList;
            }
        } catch (IllegalArgumentException | IOException e) {
            try {
                List<Timetable> firstToSecondCity = restClient.getSpeedApi().getTimetable().stream().filter(t -> t.getSource().equals(source))
                        .collect(Collectors.toList());
                List<String> listOfSecondCitiesFromfirstCity = firstToSecondCity.stream().map(Timetable::getDestination).collect(Collectors.toList());
                List<Timetable> destinationToSecondCity = restClient.getSpeedApi().getTimetable().stream().filter(t -> t.getSource().equals(destination)).collect(Collectors.toList());
                List<String> listOfSecondCitiesFromDestination = destinationToSecondCity.stream().map(Timetable::getDestination).collect(Collectors.toList());
                List<String> listSecondCities = new ArrayList<>();
                String secondCity = "";
                for (int i = 0; i < listOfSecondCitiesFromfirstCity.size(); i++) {
                    for (int j = 0; j < listOfSecondCitiesFromDestination.size(); j++) {
                        if (listOfSecondCitiesFromfirstCity.get(i).equals(listOfSecondCitiesFromDestination.get(j))) {
                            listSecondCities.add(listOfSecondCitiesFromfirstCity.get(i));
                        }
                    }

                }
                            for (int k = 0; k < listSecondCities.size(); k++) {
                                String pSc = listSecondCities.get(k);
                                List<List<Travel>> allDeparturesFrom1 = restClient.getSpeedApi().getTimetable().stream().filter(t -> t.getDestination().equals(pSc)).map(tr -> tr.getTravels()).collect(Collectors.toList());
                                String start = "";
                                for (List<Travel> ts : allDeparturesFrom1) {
                                    for (Travel travel1 : ts) {
                                        if (LocalTime.parse(travel1.getDepartureTime()).isAfter(time) || LocalTime.parse(travel1.getDepartureTime()).equals(time)) {
                                            secondCity = pSc;
                                        }
                                    }
                                }
                            }
                    String finalSecondCity1 = secondCity;
                    List<List<Travel>> travelFrom1City = firstToSecondCity.stream().filter(t -> t.getDestination().equals(finalSecondCity1) && t.getSource().equals(source)).map(Timetable::getTravels).collect(Collectors.toList());
                    LocalTime busStart = LocalTime.now();
                    List<LocalTime> allDepartureTimes = new ArrayList<>();
                    String busStop = "";
                    for (List<Travel> t : travelFrom1City) {
                        if (t.size() == 1) {
                            busStart = LocalTime.parse(t.get(0).getDepartureTime());
                            if (busStart.isAfter(time) || busStart.equals(time)) {
                                Duration duration = Duration.between(busStart, LocalTime.parse(t.get(0).getDestinationTime()));
                                returnList.add(duration.toMinutes());
                                busStop = (LocalTime.parse(t.get(0).getDestinationTime())).toString();
                            }
                        } else {
                            List<String> departureString = t.stream().map(Travel::getDepartureTime).collect(Collectors.toList());
                            for (String ds : departureString) {
                                if (LocalTime.parse(ds).isAfter(time) || busStart.equals(LocalTime.parse(busStop))) {
                                    allDepartureTimes.add(LocalTime.parse(ds));
                                }
                            }
                            busStart = allDepartureTimes.stream().min(LocalTime::compareTo).orElse(allDepartureTimes.get(0));
                            LocalTime finalBusStart = busStart;
                            busStop = t.stream().filter(tr -> tr.getDepartureTime().equals(finalBusStart.toString())).map(h -> h.getDestinationTime()).toString();
                            Duration duration = Duration.between(busStart, LocalTime.parse(busStop));
                            returnList.add(duration.toMinutes());
                        }
                    }
                    List<List<Travel>> travelFrom2toDestination = restClient.getSpeedApi().getTimetable().stream().filter(s -> s.getSource().equals(finalSecondCity1) && s.getDestination().equals(destination)).map(tr -> tr.getTravels()).collect(Collectors.toList());

                    for (List<Travel> tD : travelFrom2toDestination) {
                        if (tD.size() == 1) {
                            busStart = LocalTime.parse(tD.get(0).getDepartureTime());
                            while (busStart.isAfter(LocalTime.parse(busStop)) || busStart.equals(LocalTime.parse(busStop))) {
                                Duration duration = Duration.between(LocalTime.parse(busStop), busStart);
                                Duration duration1 = Duration.between(busStart, LocalTime.parse(tD.get(0).getDestinationTime()));
                                returnList.add(duration.toMinutes());
                                returnList.add(duration1.toMinutes());
                            }
                        } else {

                            List<String> departureString = tD.stream().map(Travel::getDepartureTime).collect(Collectors.toList());
                            for (String ds : departureString) {
                                if (busStart.isAfter(LocalTime.parse(busStop)) || busStart.equals(LocalTime.parse(busStop))) {
                                    allDepartureTimes = new ArrayList<>();
                                    allDepartureTimes.add(LocalTime.parse(ds));
                                }
                            }
                            busStart = allDepartureTimes.stream().min(LocalTime::compareTo).orElse(allDepartureTimes.get(0));
                            LocalTime finalBusStart = busStart;
                            busStop = tD.stream().filter(tr -> tr.getDepartureTime().equals(finalBusStart.toString())).map(h -> h.getDestinationTime()).toString();
                            Duration duration = Duration.between(busStart, LocalTime.parse(busStop));
                            Duration duration1 = Duration.between(LocalTime.parse(busStop), busStart);
                            returnList.add(duration.toMinutes());
                            returnList.add(duration1.toMinutes());
                        }
                    }

            } catch (DateTimeException ex) {
                throw new DateTimeException("Invalid Date format in api");
            }

        }
                return returnList;
            }
        }















