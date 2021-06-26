package com.demoapp.opsc7312task2;

public class UserSettings {
    String unitSetting;
    //String speedCameras;
    //String speedLimits;
    String traffic;
    //String roadConstruction;

    public UserSettings()
    {
        //Empty constructor for data retrieval
    }

    public UserSettings(String unitSetting, String traffic)
    {
        this.unitSetting = unitSetting;
        this.traffic = traffic;
    }


    public String getUnitSetting()
    {
        return unitSetting;
    }

    public void setUnitSetting(String unitSetting)
    {
        this.unitSetting = unitSetting;
    }


    public String getTraffic()
    {
        return traffic;
    }

    public void setTraffic(String traffic)
    {
        this.traffic = traffic;
    }
    
}
