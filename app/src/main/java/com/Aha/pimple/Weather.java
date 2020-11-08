package com.Aha.pimple;


//JSON Object를 만들어주는 클래스이다. 현제 json 이 가지고 있는 객체가 3개 country,weather,temperature
//이므로 이것을 만들어준다.

public class Weather {
    private String country;
    private String weather;
    private String temperature;

    public Weather(String country, String weather, String temperature){
        this.country = country;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "country='" + country + '\'' +
                ", weather='" + weather + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }
}
