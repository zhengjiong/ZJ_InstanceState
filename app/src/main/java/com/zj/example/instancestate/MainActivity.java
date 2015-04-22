package com.zj.example.instancestate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Activity和Fragment狀態保存與恢復Demo
 *
 * create by zhengjiong
 * Date: 2015-04-21
 * Time: 08:38
 */
public class MainActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                startActivity(new Intent(MainActivity.this, ActivityTest.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(MainActivity.this, FragmentTest.class));
                break;
        }
    }
}
