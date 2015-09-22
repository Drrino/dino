package com.dinodemo.activity.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import com.dinodemo.receiver.AlarmReceiver;
import com.dinodemo.receiver.ClockReceiver;
import java.util.Calendar;

/**
 * Created by Coder on 2015/9/9.
 */
public class TabAlarm extends BaseTabFragment implements View.OnClickListener {
  @Bind(R.id.lvAlarmList) ListView lvAlarmList;
  @Bind(R.id.btnAddAlarm) Button btnAddAlarm;

  private AlarmManager alarmManager;
  private ArrayAdapter<AlarmData> adapter;
  private static final String KEY_ALARM_LIST = "alarmList";
  public boolean isClick = true;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.tabalarm, null);
    ButterKnife.bind(this, rootView);
    init();
    return rootView;
  }

  private void deleteAlarm(int position) {
    AlarmData ad = adapter.getItem(position);
    adapter.remove(ad);
    saveAlarmList();
    alarmManager.cancel(PendingIntent.getBroadcast(getContext(), ad.getId(),
        new Intent(getContext(), ClockReceiver.class), 0));
  }

  private void init() {
    alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
    lvAlarmList.setAdapter(adapter);
    readSavedAlarmList();

    btnAddAlarm.setOnClickListener(this);
    lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override public boolean onItemLongClick(AdapterView<?> parent, View view, final int position,
          long id) {
        new AlertDialog.Builder(getContext()).setTitle("Option")
            .setItems(new CharSequence[] { "Do you want to delete?" },
                new DialogInterface.OnClickListener() {
                  @Override public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "彩蛋~~~", Toast.LENGTH_SHORT).show();
                  }
                })
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                deleteAlarm(position);
              }
            })
            .setNegativeButton("No", null)
            .show();
        return true;
      }
    });
  }

  private void readSavedAlarmList() {
    SharedPreferences sp =
        getContext().getSharedPreferences(TabAlarm.class.getName(), Context.MODE_PRIVATE);
    String content = sp.getString(KEY_ALARM_LIST, null);

    if (content != null) {
      String[] timeStrings = content.split(",");
      for (String string : timeStrings) {
        adapter.add(new AlarmData(Long.parseLong(string)));
      }
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnAddAlarm:
        addAlarm();
        break;
    }
  }

  private void addAlarm() {
    Calendar c = Calendar.getInstance();
    new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
      @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
          Calendar calendar = Calendar.getInstance();
          calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
          calendar.set(Calendar.MINUTE, minute);
          calendar.set(Calendar.SECOND, 0);
          calendar.set(Calendar.MILLISECOND, 0);

          Calendar currentTime = Calendar.getInstance();
          if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
            calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
          }

          AlarmData ad = new AlarmData(calendar.getTimeInMillis());
          adapter.add(ad);
          alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, ad.getTime(), 5 * 60 * 1000,
              PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), ClockReceiver.class), 0));
          saveAlarmList();
        }
    }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
  }

  private void saveAlarmList() {
    SharedPreferences.Editor editor =
        getContext().getSharedPreferences(TabAlarm.class.getName(), Context.MODE_PRIVATE).edit();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < adapter.getCount(); i++) {
      sb.append(adapter.getItem(i).getTime()).append(",");
    }

    if (sb.length() > 1) {
      String content = sb.toString().substring(0, sb.length() - 1);
      editor.putString(KEY_ALARM_LIST, content);
    } else {
      editor.putString(KEY_ALARM_LIST, null);
    }
    editor.commit();
  }
}
