package com.dinodemo.activity.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dinodemo.activity.config.Config;

/**
 * Created by Coder on 2015/9/24.
 */
public class GameItem extends FrameLayout {

  //Item显示的数字
  private int mCardShowNum;
  //数字Title
  private TextView mTvNum;

  private LayoutParams mParams;

  public GameItem(Context context, int cardShowNum) {
    super(context);
    this.mCardShowNum = cardShowNum;

    initCardItem();
  }

  private void initCardItem() {
    //设置面板背景色
    setBackgroundColor(Color.parseColor("#e0e0e0"));
    mTvNum = new TextView(getContext());
    setNum(mCardShowNum);
    //修改行 * 列时字体过大
    int gameLines = Config.mSp.getInt(Config.KEY_GAME_LINES, 4);
    if (gameLines == 4) {
      mTvNum.setTextSize(35);
    } else if (gameLines == 5) {
      mTvNum.setTextSize(25);
    } else if (gameLines == 6) {
      mTvNum.setTextSize(20);
    }
    TextPaint tp = mTvNum.getPaint();
    tp.setFakeBoldText(true);
    mTvNum.setGravity(Gravity.CENTER);
    mParams =
        new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    mParams.setMargins(5, 5, 5, 5);
    addView(mTvNum, mParams);
  }

  public View getItemView(){
    return mTvNum;
  }

  public int getNum(){
    return mCardShowNum;
  }

  public void setNum(int cardShowNum) {
    this.mCardShowNum = cardShowNum;
    if (cardShowNum == 0) {
      mTvNum.setText("");
    } else {
      mTvNum.setText("" + cardShowNum);
    }
    //设置颜色
    switch (cardShowNum) {
      case 0:
        mTvNum.setBackgroundColor(Color.parseColor("#bdbdbd"));
        break;
      case 2:
        mTvNum.setBackgroundColor(Color.parseColor("#fbe9e7"));
        break;
      case 4:
        mTvNum.setBackgroundColor(Color.parseColor("#ffe0b2"));
        break;
      case 8:
        mTvNum.setBackgroundColor(Color.parseColor("#fff2c17a"));
        break;
      case 16:
        mTvNum.setBackgroundColor(Color.parseColor("#fff59667"));
        break;
      case 32:
        mTvNum.setBackgroundColor(Color.parseColor("#fff68c6f"));
        break;
      case 64:
        mTvNum.setBackgroundColor(Color.parseColor("#fff66e3c"));
        break;
      case 128:
        mTvNum.setBackgroundColor(Color.parseColor("#ffedcf74"));
        break;
      case 256:
        mTvNum.setBackgroundColor(Color.parseColor("#ffedcc64"));
        break;
      case 512:
        mTvNum.setBackgroundColor(Color.parseColor("#ffedc854"));
        break;
      case 1024:
        mTvNum.setBackgroundColor(Color.parseColor("#ffedc54f"));
        break;
      case 2048:
        mTvNum.setBackgroundColor(Color.parseColor("#ffedc32e"));
        break;
    }
  }
}
