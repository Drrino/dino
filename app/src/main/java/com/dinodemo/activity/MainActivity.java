package com.dinodemo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import com.dinodemo.utils.AlarmManagerUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import java.util.Random;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String SHOWCASE_ID = "dino";
  @Bind(R.id.mobileNumber) EditText mobileNumber;
  @Bind(R.id.password) EditText password;
  @Bind(R.id.test) EditText editCode;
  @Bind(R.id.tv_code) TextView getCode;
  @Bind(R.id.register) Button register;
  @Bind(R.id.login) Button login;
  @Bind(R.id.tl_custom) Toolbar toolbar;
  @Bind(R.id.dl_left) DrawerLayout mDrawerLayout;

  private SharedPreferences sp;
  private String mPassword;
  private String mMobileNumber;
  private String mEditCode;

  private ActionBarDrawerToggle mDrawerToggle;
  private StringBuffer sb;
  private String codeNumber;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    codeNumber = getCodeNumber();
    getCode.setText(codeNumber);
    setToolBar();
    sp = getSharedPreferences("config", MODE_PRIVATE);
    login.setOnClickListener(this);
    register.setOnClickListener(this);
    getCode.setOnClickListener(this);
    presentShowcaseSequence();
    FloatingActionMenu();

    AlarmManagerUtils.register(this);
  }

  private void FloatingActionMenu() {
    final View actionB = findViewById(R.id.action_b);
    FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
    actionC.setTitle("Hide/Show");
    actionC.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
      }
    });

    final FloatingActionsMenu menuMultipleActions =
        (FloatingActionsMenu) findViewById(R.id.multiple_actions);
    menuMultipleActions.addButton(actionC);

    ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
    drawable.getPaint().setColor(Color.parseColor("#ffffff"));

    final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
    actionA.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        actionA.setTitle("clickedA");
      }
    });
  }

  private void presentShowcaseSequence() {
    ShowcaseConfig config = new ShowcaseConfig();
    config.setDelay(500); // half second between each showcase view
    MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
    sequence.setConfig(config);
    sequence.addSequenceItem(register, "This is register button", "GOT IT");
    sequence.addSequenceItem(login, "This is login button", "GOT IT");
    sequence.start();
  }

  private void setToolBar() {
    toolbar.setTitle("dino");
    toolbar.setTitleTextColor(Color.parseColor("#004d40"));
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mDrawerToggle =
        new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
    mDrawerToggle.syncState();
    mDrawerLayout.setDrawerListener(mDrawerToggle);
  }

  /**
   * 注册点击事件
   */
  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.register:
        mPassword = password.getText().toString().trim();
        mMobileNumber = mobileNumber.getText().toString().trim();
        mEditCode = editCode.getText().toString().trim();
        if (!TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(mMobileNumber)) {
          if (mEditCode.equalsIgnoreCase(codeNumber) && codeNumber.length() == 4) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("password", mPassword);
            editor.putString("mobileNumber", mMobileNumber);
            editor.commit();
            showDialog("用户注册成功");
            editCode.getText().clear();
            mobileNumber.getText().clear();
            password.getText().clear();
            codeNumber = getCodeNumber();
            getCode.setText(codeNumber);
          } else {
            showDialog("验证码输入错误");
          }
        } else if (!TextUtils.isEmpty(mMobileNumber) && mMobileNumber.equals(
            sp.getString("mobileNumber", ""))) {
          showDialog("用户名已注册");
        } else {
          showDialog("用户名填写不完整");
        }
        break;
      case R.id.login:
        mPassword = password.getText().toString().trim();
        mMobileNumber = mobileNumber.getText().toString().trim();
        mEditCode = editCode.getText().toString().trim();
        if (mMobileNumber.equals(sp.getString("mobileNumber", "")) && mPassword.equals(
            sp.getString("password", ""))) {
          if (mEditCode.equalsIgnoreCase(codeNumber) && codeNumber.length() == 4) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
          } else {
            showDialog("请输入验证码");
          }
        } else if (!mMobileNumber.equals(sp.getString("mobileNumber", "")) && !mPassword.equals(
            sp.getString("password", ""))) {
          showDialog("先注册");
        } else {
          showDialog("信息错误");
        }
        break;
      case R.id.tv_code:
        codeNumber = "";
        codeNumber = getCodeNumber();
        getCode.setText(codeNumber);
        break;
    }
  }

  private String getCodeNumber() {
    Random rdm = new Random();
    char[] code = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
        'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C',
        'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
        'W', 'X', 'Y', 'Z'
    };
    if (sb == null) {
      sb = new StringBuffer();
    }
    sb.setLength(0);
    for (int i = 0; i < code.length; i++) {
      sb.append(code[rdm.nextInt(code.length)]);
      if (sb.length() == 4) break;
    }
    return sb.toString().trim();
  }

  /**
   * 显示Dialog
   */
  private void showDialog(String info) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("提示")
        .setMessage(info)
        .setPositiveButton("确定", null)
        .setNegativeButton("取消", null);
    builder.show();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    return super.onOptionsItemSelected(item);
  }
}
