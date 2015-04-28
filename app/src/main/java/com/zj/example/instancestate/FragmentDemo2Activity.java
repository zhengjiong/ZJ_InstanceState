package com.zj.example.instancestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * create by zhengjiong
 * Date: 2015-04-27
 * Time: 08:00
 */
public class FragmentDemo2Activity extends ActionBarActivity {
    private String TAG = "FragmentDemo2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test_layout);
        Log.i(TAG, "onCreate");
        MyFragment myFragment = (MyFragment) getSupportFragmentManager().findFragmentById(R.id.place_holder);

        if (myFragment == null) {
            Log.i(TAG, "myFragment = null");
            myFragment = new MyFragment();

            /**
             * commit放在if裡面還外面的效果一樣,
             * 屏幕旋轉后,執行效果如下:
             *
             * 04-27 08:38:44.110  26075-26075/com.zj.example.instancestate I/MyFragment﹕ MyFragment onSaveInstanceState
             * 04-27 08:38:44.110  26075-26075/com.zj.example.instancestate I/FragmentDemo2Activity﹕ onSaveInstanceState
             * 04-27 08:38:44.110  26075-26075/com.zj.example.instancestate I/MyFragment﹕ onDestroyView
             * 04-27 08:38:44.162  26075-26075/com.zj.example.instancestate I/FragmentDemo2Activity﹕ onCreate
             * 04-27 08:38:44.167  26075-26075/com.zj.example.instancestate I/MyFragment﹕ onCreateView
             * 04-27 08:38:44.169  26075-26075/com.zj.example.instancestate I/MyFragment﹕ MyFragment savedInstanceState != null

             */
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.place_holder, myFragment)
                    .commit();
        }

        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.place_holder, myFragment)
                .commit();*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    static public class MyFragment extends Fragment {
        private static final String TAG = "MyFragment";
        private View mRootView;
        private TextView mTxtContent;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.i(TAG, "onCreateView");
            mRootView = inflater.inflate(R.layout.fragment, container, false);
            return mRootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            mTxtContent = (TextView) mRootView.findViewById(R.id.txt);

            if (savedInstanceState == null) {
                Log.i(TAG, "MyFragment savedInstanceState == null");
                mTxtContent.setText("MyFragment savedInstanceState == null");
            } else {
                /**
                 * 恢復狀態的時候,獲取content的值并設置到textview中
                 */
                Log.i(TAG, "MyFragment savedInstanceState != null");
                mTxtContent.setText("MyFragment savedInstanceState != null, outState = " + savedInstanceState.getString("content"));
            }
        }

        /**
         * Fragment保存狀態的時候增加一個變量content
         * @param outState
         */
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            Log.i(TAG, "MyFragment onSaveInstanceState");
            outState.putString("content", "zhengjiong");
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            Log.i(TAG, "onDestroyView");
        }
    }
}
