package com.dinodemo.activity.clock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dinodemo.R;

/**
 * Created by Coder on 2015/9/9.
 */
public class TabAlarm extends BaseTabFragment {

  private TextView msg;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.tabalarm, null);
    msg = (TextView) rootView.findViewById(R.id.text);
    //		Bundle ga = getArguments();
    //		String val = ga.getString("pretty");
    //		msg.setText(val);
    return rootView;
  }
}
