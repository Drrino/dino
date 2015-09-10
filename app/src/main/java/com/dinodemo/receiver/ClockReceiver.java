package com.dinodemo.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.dinodemo.activity.clock.PlayAlarmAty;
import com.dinodemo.activity.clock.TabAlarm;

/**
 * Created by Coder on 2015/9/10.
 */
public class ClockReceiver extends BroadcastReceiver {
  @Override public void onReceive(Context context, Intent intent) {

    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    am.cancel(PendingIntent.getBroadcast(context, getResultCode(),
        new Intent(context, ClockReceiver.class), 0));

    Intent i = new Intent(context, PlayAlarmAty.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
  }
}
