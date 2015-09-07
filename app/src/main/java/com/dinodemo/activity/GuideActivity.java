package com.dinodemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import com.dinodemo.utils.PrefUtils;
import java.util.ArrayList;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by Coder on 2015/8/30.
 */
public class GuideActivity extends AppCompatActivity {
  private static final String INTO = "0";

  @Bind(R.id.vp_guide) ViewPager viewPager;
  @Bind(R.id.button) Button button;

  private static final int[] mImageIds =
      new int[] { R.mipmap.num1, R.mipmap.num2, R.mipmap.num3 };
  private ArrayList<ImageView> mImageView;
  private ImageView image;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.guide_pager);
    ButterKnife.bind(this);
    initView();

    viewPager.setAdapter(new GuideAdapter());
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      //选中某页
      @Override public void onPageSelected(int position) {
        if (position==mImageView.size() - 1){
          presentShowcaseView(0);
          button.setVisibility(View.VISIBLE);
          button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              PrefUtils.setBoolean(GuideActivity.this,"is_show",true);
              Intent intent = new Intent(GuideActivity.this,MainActivity.class);
              startActivity(intent);
              finish();
            }
          });
        }
      }
      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }

  private void presentShowcaseView(int withDelay) {
    new MaterialShowcaseView.Builder(this).setTarget(button)
        .setDismissText("GOT IT")
        .setContentText("This is some amazing feature you should know about")
        .setDelay(withDelay) // optional but starting animations immediately in onCreate can make them choppy
        .singleUse(INTO) // provide a unique ID used to ensure it is only shown once
        .show();
  }

  private void initView() {
    mImageView = new ArrayList<>();
    for (int i = 0; i < mImageIds.length; i++) {
      image = new ImageView(this);
      image.setBackgroundResource(mImageIds[i]);
      mImageView.add(image);
    }
  }

  /**
   * ViewPager适配器
   */
  class GuideAdapter extends PagerAdapter {

    @Override public int getCount() {
      return mImageIds.length;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      container.addView(mImageView.get(position));
      return mImageView.get(position);
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
     container.removeView((View) object);
    }
  }
}
