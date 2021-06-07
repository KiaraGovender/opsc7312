package com.demoapp.opsc7312task2;

public class UserFavorites
{
    String locationName;
    Double latitude;
    Double longitude;

    public  UserFavorites()
    {
        //Empty Constructor
    }

    public UserFavorites(String locationName, Double latitude, Double longitude)
    {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString()
    {
        return "UserFavorites{" +
                "Location ='" + locationName + '\'' +
                ", Latitude=" + latitude +
                ", Longitude=" + longitude +
                '}';
    }
}
