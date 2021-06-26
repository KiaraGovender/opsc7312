package com.demoapp.opsc7312task2;

public class UserLandmarks {
    String preferredLandmark;

    public UserLandmarks()
    {
        //Empty Constructor
    }

    public UserLandmarks(String preferredLandmark)
    {
        this.preferredLandmark = preferredLandmark;
    }

    public String getPreferredLandmark()
    {
        return preferredLandmark;
    }

    public void setPreferredLandmark(String preferredLandmark)
    {
        this.preferredLandmark = preferredLandmark;
    }

    @Override
    public String toString()
    {
        return "UserLandmarks{" +
                "Preferred Landmark = '" + preferredLandmark + "' +'}";
    }
}
