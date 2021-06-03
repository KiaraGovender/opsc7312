package com.demoapp.opsc7312task2;

public class UserLogin
{
    String fullName;
    String emailAddress;

    public UserLogin(String fullName, String emailAddress)
    {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String toString()
    {
        return "Full Name " + fullName;
    }
}
