package com.zj.example.instancestate;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * onSaveInstanceState:
 * 1.长按HOME键，选择运行其他的程序时
 * 2.按下电源按键（关闭屏幕显示）时
 * 3.从activity A中启动一个新的activity时
 * 4.屏幕方向切换时，例如从竖屏切换到横屏时在屏幕切换之前，系统会销毁activity A，
 * 在屏幕切换之后系统又会自动地创建activity A，所以onSaveInstanceState()一定会被执行，且也一定会执行onRestoreInstanceState()。
 *
 *
 * 如果需要保存额外的数据时, 就需要覆写onSaveInstanceState()方法。
 * 大家需要注意的是：onSaveInstanceState()方法只适合保存瞬态数据,
 * 比如UI控件的状态, 成员变量的值等，而不应该用来保存持久化数据，
 * 持久化数据应该当用户离开当前的 activity时，在 onPause() 中保存（比如将数据保存到数据库或文件中）。
 * 说到这里，还要说一点的就是在onPause()中不适合用来保存比较费时的数据，所以这点要理解。
 *
 * 由于onSaveInstanceState()方法方法不一定会被调用, 因此不适合在该方法中保存持久化数据,
 * 例如向数据库中插入记录等. 保存持久化数据的操作应该放在onPause()中。若是永久性值，
 * 则在onPause()中保存；若大量，则另开线程吧，别阻塞UI线程。
 *
 *
 *
 * onRestoreInstanceState()什么时候调用?:
 * onRestoreInstanceState()被调用的前提是，activity A“确实”被系统销毁了，而如果仅仅是停留在有这种可能性的情况下，
 * 则该方法不会被调用，例如，当正在显示activity A的时候，用户按下HOME键回到主界面，然后用户紧接着又返回到activity A，
 * 这种情况下activity A一般不会因为内存的原因被系统销毁，故activity A的onRestoreInstanceState方法不会被执行 此也说明上二者，
 * 大多数情况下不成对被使用。
 *
 * create by zhengjiong
 * Date: 2015-04-20
 * Time: 08:13
 */
public class ActivityTest extends ActionBarActivity {
    private static final String TAG = "MainActivity";

    private TextView mTxtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);

        mTxtContent = (TextView) findViewById(R.id.content);

        /**
         * 一般不用onRestoreInstanceState方法，而在onCreate方法直接处理。
         */
        if (savedInstanceState != null) {
            Log.i(TAG, "savedInstanceState != null");
            mTxtContent.setText("savedInstanceState != null");
        } else {
            Log.i(TAG, "savedInstanceState == null");
            mTxtContent.setText("savedInstanceState == null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 5.0.1系統onSaveInstanceState發生在onPause之後,onStop之前
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(this, "onSaveInstanceState", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }
}
