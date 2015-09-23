package com.dinodemo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import com.dinodemo.activity.widget.RecyclerViewFastScroller;
import com.dinodemo.adapter.ContactAdapter;

/**
 * Created by Coder on 2015/9/23.
 */
public class FastScrollerActivity extends AppCompatActivity {
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.refresh) SwipeRefreshLayout mSwipeRefreshLayout;
  @Bind(R.id.fastscroller) RecyclerViewFastScroller fastScroller;
  @Bind(R.id.fab) FloatingActionButton fab;
  @Bind(R.id.recyclerView) RecyclerView recyclerView;

  private ContactAdapter mAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.recyclerviewfastdcroller);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    initSwipeRefresh();
    initFab();
    initRecycleView();
  }

  private void initSwipeRefresh() {
    mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh1, R.color.refresh2,
        R.color.refresh3);
  }

  private void initRecycleView() {
    //设置RecyclerView的布局管理
    LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(linearLayoutManager);
    mAdapter = new ContactAdapter();
    recyclerView.setAdapter(mAdapter);
    fastScroller.setRecyclerView(recyclerView);
    fastScroller.setViewsToUse(R.layout.recycle_scroller, R.id.fastScroller_bubble,
        R.id.fastScroller_handle);

    fastScroller.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_MOVE:
            mSwipeRefreshLayout.setEnabled(false);
            break;
          case MotionEvent.ACTION_UP:
          case MotionEvent.ACTION_CANCEL:
            mSwipeRefreshLayout.setEnabled(true);
            break;
        }
        return false;
      }
    });
  }

  private void initFab() {
    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Snackbar.make(findViewById(R.id.coordinator), "This is SnackBar", Snackbar.LENGTH_SHORT)
            .setAction("Ok", new View.OnClickListener() {
              @Override public void onClick(View v) {
                Toast.makeText(FastScrollerActivity.this, "SnackBar Action", Toast.LENGTH_SHORT).show();
              }
            })
            .show();
      }
    });
  }
}
