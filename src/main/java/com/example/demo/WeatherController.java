package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final String WEATHER_API  = "https://api.openweathermap.org/data/2.5/weather";
    private final String COORDS_API   = "https://api.openweathermap.org/geo/1.0/direct";
    private final String API_KEY      = "777ee6d51ab7d03babc6826c0b32e318";

    @GetMapping()
    public String getMethodName(@RequestParam String latitude, @RequestParam String longitude) {
        RestTemplate restTemplate = new RestTemplate();
        String url = WEATHER_API + "?lat=" + latitude + "&lon=" + longitude 
            + "&units=metric" + "&appid=" + API_KEY;
        String result = restTemplate.getForObject(url, String.class);
        if (result != null) {
            return result;
        }    
        return "Error: Unable to fetch weather data.";
    }  

    @GetMapping("/city")
    public String getCityWeather(@RequestParam String city_ISO) {
        RestTemplate restTemplate = new RestTemplate();
        String url = COORDS_API + "?q=" + city_ISO + "&limit=1" + "&appid=" + API_KEY;

        CoordsRequest[] requests = restTemplate.getForObject(url, CoordsRequest[].class);
        if (null == requests || requests.length == 0) {
            return "Error: Unable to fetch coordinates for the specified city.";
        }

        WeatherRequest request = new WeatherRequest();
        request.setLat(requests[0].getLat());
        request.setLon(requests[0].getLon());

        RestTemplate restTemplate2 = new RestTemplate();
        String url2 = WEATHER_API + "?lat=" + request.getLat() + "&lon=" + request.getLon() 
            + "&units=metric" + "&appid=" + API_KEY;

        String cityweather = restTemplate2.getForObject(url2, String.class);

        return cityweather;



    }
}
