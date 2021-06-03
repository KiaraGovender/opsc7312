package com.demoapp.opsc7312task2;

import android.content.Context;
import android.content.Intent;

public class IntentHelper
{
    public void openIntent(Context context, Class newActivity)
    {
        Intent i = new Intent(context, newActivity);
        i.putExtra("New Activity", newActivity);
        context.startActivity(i);
    }
}
