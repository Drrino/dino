package com.dinodemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;
import cn.aigestudio.datepicker.views.DatePicker;
import com.dinodemo.R;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Coder on 2015/9/8.
 */
public class DateActivity extends AppCompatActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.date_pager);

    Calendar c = Calendar.getInstance();
    int i = c.get(Calendar.MONTH);
    // 默认多选模式
    DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
    picker.setDate(2015, i);
    picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
      @Override public void onDateSelected(List<String> date) {
        String result = "";
        Iterator iterator = date.iterator();
        while (iterator.hasNext()) {
          result += iterator.next();
          if (iterator.hasNext()) {
            result += "\n";
          }
        }
        Toast.makeText(DateActivity.this, result, Toast.LENGTH_LONG).show();
      }
    });
  }
}
