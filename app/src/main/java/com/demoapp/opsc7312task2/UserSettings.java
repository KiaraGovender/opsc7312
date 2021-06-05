package com.demoapp.opsc7312task2;

public class UserSettings {
    String unitSetting;
    String speedCameras;
    String speedLimits;
    String traffic;
    String roadConstruction;

    public UserSettings()
    {
        //Empty constructor for data retrieval
    }

    public UserSettings(String unitSetting, String speedCameras, String speedLimits, String traffic, String roadConstruction)
    {
        this.unitSetting = unitSetting;
        this.speedCameras = speedCameras;
        this.speedLimits = speedLimits;
        this.traffic = traffic;
        this.roadConstruction = roadConstruction;
    }


    public String getUnitSetting()
    {
        return unitSetting;
    }

    public void setUnitSetting(String unitSetting)
    {
        this.unitSetting = unitSetting;
    }

    public String getSpeedCameras()
    {
        return speedCameras;
    }

    public void setSpeedCameras(String speedCameras)
    {
        this.speedCameras = speedCameras;
    }

    public String getSpeedLimits()
    {
        return speedLimits;
    }

    public void setSpeedLimits(String speedLimits)
    {
        this.speedLimits = speedLimits;
    }

    public String getTraffic()
    {
        return traffic;
    }

    public void setTraffic(String traffic)
    {
        this.traffic = traffic;
    }

    public String getRoadConstruction()
    {
        return roadConstruction;
    }

    public void setRoadConstruction(String roadConstruction)
    {
        this.roadConstruction = roadConstruction;
    }
}
