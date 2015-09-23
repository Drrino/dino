package com.dinodemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dinodemo.activity.widget.RecyclerViewFastScroller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Coder on 2015/9/22.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements
    RecyclerViewFastScroller.BubbleTextGetter{
  private static final int SIZE = 200;
  private Context mContext;
  private List<String> items;

  public ContactAdapter() {
    List<String> items = new ArrayList<>();
    Random r = new Random();
    for (int i = 0; i < SIZE; i++)
      items.add(((char) ('A' + r.nextInt('Z' - 'A'))) + " " + Integer.toString(i));
    Collections.sort(items);
    this.items = items;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    String text = items.get(position);
    holder.setText(text);
  }

  @Override public int getItemCount() {
    return items.size();
  }

  @Override public String getTextToShowInBubble(int pos) {
    return Character.toString(items.get(pos).charAt(0));
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      this.textView = (TextView) itemView.findViewById(android.R.id.text1);
    }

    public void setText(CharSequence text) {
      textView.setText(text);
    }
  }
}
