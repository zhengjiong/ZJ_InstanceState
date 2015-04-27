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
 *
 * 參考: http://www.soloho.cc/blog/how-to-save-fragment_state
 * 保存Fragment的狀態
 * 利用Activity的onSaveInstanceState保存Fragment实例的数据，并在onCreate里面恢复数据。
 *
 *
 * 當屏幕旋轉的時候調用的順序:
 * MyFragment﹕ MyFragment onSaveInstanceState
 * FragmentDemo﹕ FragmentDemo onSaveInstanceState
 * MyFragment﹕ onDestroyView
 * FragmentDemo﹕ FragmentDemo savedInstanceState != null
 * MyFragment﹕ onCreateView
 * MyFragment﹕ MyFragment savedInstanceState != null
 *
 *
 * create by zhengjiong
 * Date: 2015-04-21
 * Time: 08:39
 */
public class FragmentDemoActivity extends ActionBarActivity {
    private static final String TAG = "FragmentDemo";

    private MyFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test_layout);


        if (savedInstanceState == null) {
            Log.i(TAG, "FragmentDemo savedInstanceState == null");
            mFragment = new MyFragment();

            /**
             * commit放在if裡面還外面的效果一樣,
             */
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.place_holder, mFragment, "myFragment")
                    .commit();
        } else {
            //利用onSaveInstanceState保存Fragment实例的数据，并在onCreate里面恢复数据。
            Log.i(TAG, "FragmentDemo savedInstanceState != null");

            /**
             * 必須使用getFragment獲取mFragment對象,
             * 如果這裡想用成員變量mFragment是不行的,屏幕旋轉后或者被系統回收后mFragment已經是一個null對象,
             * 下面的replace會NullPointerException,
             */
            mFragment = (MyFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "fragment");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "FragmentDemo onSaveInstanceState");

        /**
         * 如果想要在屏幕旋轉后或者Activity和Fragment被系統回收后恢復以前的狀態,必須在
         * onSaveInstanceState保存Fragment实例的数据，并在onCreate里面恢复数据。
         * 保存Fragment的方法為putFragment(),恢復Fragment的方法為getFragment()
         *
         * putFragment(Bundle bundle, String key, Fragment fragment)
         * 方法解釋:
         * Put a reference to a fragment in a Bundle. This Bundle can be persisted as saved state,
         * and when later restoring getFragment(Bundle, String) will return the current instance of the same fragment.
         * 翻譯:把fragment實例放入Bundle中,在還原的時候,可以使用getFragment(Bundle, String)恢復出相同的Fragment
         */
        getSupportFragmentManager().putFragment(outState, "fragment", mFragment);
    }

    static public class MyFragment extends Fragment{
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
