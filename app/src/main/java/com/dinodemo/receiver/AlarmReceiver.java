package com.dinodemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.dinodemo.activity.MainActivity;
import com.dinodemo.R;
import com.dinodemo.utils.HeadsUpUtils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
        HeadsUpUtils.show(context, MainActivity.class, context.getString(R.string.headsup_title),
            context.getString(R.string.headsup_content), R.mipmap.ic_meizhi_150602,
            R.mipmap.ic_female, 123123);
    }
}