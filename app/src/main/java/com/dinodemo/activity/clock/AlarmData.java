package com.dinodemo.activity.clock;

import java.util.Calendar;

/**
 * Created by Coder on 2015/9/10.
 */
public class AlarmData {
  private String timeLabel="";
  private long time = 0;
  private Calendar date;

  public AlarmData(long time) {
    this.time = time;

    date = Calendar.getInstance();
    date.setTimeInMillis(time);

    timeLabel =
        String.format("%d月%d日 %02d:%02d", date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));
  }

  public String getTimeLabel() {
    return timeLabel;
  }

  public long getTime() {
    return time;
  }

  public int getId() {
    return (int) (getTime() / 1000 / 60);
  }

  @Override public String toString() {
    return getTimeLabel();
  }
}
