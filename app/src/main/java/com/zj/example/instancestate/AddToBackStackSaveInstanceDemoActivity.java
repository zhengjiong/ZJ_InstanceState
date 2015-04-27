package com.zj.example.instancestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 * 此Demo適用於保存back stack中的Fragment數據,如果沒有Back Stack用FragmentDemoActivity方式來保存Fragment狀態
 *
 * 參考: http://www.2cto.com/kf/201503/386389.html
 *      https://github.com/nuuneoi/StatedFragment
 *
 * 使用replace:
 * addToBackStack将当前的事务添加到了回退栈，所以FragmentA实例不会被销毁，
 * 但是视图层次依然会被销毁，即会调用onDestoryView和onCreateView,
 * 不使用addToBackStack的話FragmentA实例会被销毁。
 *
 * 如果不希望视图重绘:
 * 使用add不會讓实例FragmentA销毁,也不會onDestroyView,可以用hide(this),再add FragmentB
 * 用户Back回来时，数据还在
 *
 *
 *
 *
 * 保存Back Stack中Fragment的狀態:
 * FragmentA在onDestroyView中保存一個bundle在Argument中,
 * FragmentA在onCreateView或者onActivityCreated中獲取Argument恢復數據
 *
 * create by zhengjiong
 * Date: 2015-04-21
 * Time: 08:39
 */
public class AddToBackStackSaveInstanceDemoActivity extends ActionBarActivity {
    private static final String TAG = "SaveInstanceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test_layout);
        Log.i(TAG, "onCreate");


        /**
         * 這裡要注意:
         * savedInstanceState==null的時候才實例化FragmentA
         */
        if (savedInstanceState == null) {
            Log.i(TAG, "savedInstanceState == null");

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.place_holder, FragmentA.newInstance())
                    .commit();
        } else {
            Log.i(TAG, "savedInstanceState != null");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

    }

    static public class FragmentA extends Fragment{
        private static final String TAG = "FragmentA";
        private View mRootView;
        private TextView mTxtContent;
        private Button mBtn;

        public static FragmentA newInstance(){
            FragmentA fragmentA = new FragmentA();
            if (fragmentA.getArguments() == null) {
                fragmentA.setArguments(new Bundle());
            }
            return fragmentA;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.i(TAG, "onCreateView");
            mRootView = inflater.inflate(R.layout.fragment_a, container, false);
            return mRootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.i(TAG, "onActivityCreated");
            mTxtContent = (TextView) mRootView.findViewById(R.id.txt);
            mBtn = (Button) mRootView.findViewById(R.id.btn);
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /**
                     * 使用replace:
                     * addToBackStack将当前的事务添加到了回退栈，所以FragmentA实例不会被销毁，
                     * 但是视图层次依然会被销毁，即会调用onDestoryView和onCreateView,
                     * 不使用addToBackStack的話FragmentA实例会被销毁。
                     *
                     * 使用add不會讓实例FragmentA销毁,也不會onDestroyView,可以用hide(this),再add FragmentB
                     *
                     * replace(R.id.place_holder, new FragmentB()).addToBackStack(null),
                     * 執行后不會執行Activity和FragmentA的onSaveInstanceState方法,
                     * 但是會執行FragmentA的onDestroyView,所以需要在onDestroyView中保存狀態, 用户Back回来时，数据还在
                     *
                     *
                     * FragmentA﹕ onDestroyView
                     * FragmentB﹕ onCreateView
                     * FragmentB﹕ onActivityCreated
                     * FragmentB﹕ savedInstanceState == null
                     */
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.place_holder, new FragmentB())
                            .addToBackStack(null)
                            .commit();

                    /**
                     * 啟動一個Activity,會先執行Fragment的onSaveInstanceState,再執行Activity的onSaveInstanceState
                     *  FragmentA﹕ onSaveInstanceState
                     *  FragmentDemo2Activity﹕ onSaveInstanceState
                     */
                    //startActivity(new Intent(getActivity(), MainActivity.class));
                }
            });
            /*if (savedInstanceState == null) {
                Log.i(TAG, "savedInstanceState == null");
                mTxtContent.setText("savedInstanceState == null");
            } else {
                Log.i(TAG, "savedInstanceState != null");
                mTxtContent.setText("savedInstanceState != null, outState = " + savedInstanceState.getString("content"));
            }*/
            Bundle args = getArguments();
            Bundle bundle = args.getBundle("bundle");
            if (bundle != null) {
                Log.i(TAG, "bundle != null");
                mTxtContent.setText("savedInstanceState != null, args = " + bundle.getString("content"));
            } else {
                Log.i(TAG, "bundle = null");
                mTxtContent.setText("savedInstanceState == null");
            }
        }

        /**
         * mBtn的click不會觸發onSaveInstanceState事件,需要在onDestroyView中保存狀態
         * @param outState
         */
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            Log.i(TAG, "onSaveInstanceState");
        }


        /**
         * 由於mBtn的click不會觸發onSaveInstanceState事件,所以需要
         * 在onDestroyView中保存狀態
         */
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            Log.i(TAG, "onDestroyView");
            Bundle bundle = new Bundle();
            bundle.putString("content", "zhengjiong");

            getArguments().putBundle("bundle", bundle);
        }
    }

    static public class FragmentB extends Fragment{
        private static final String TAG = "FragmentB";
        private View mRootView;
        private Button mBtn;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.i(TAG, "onCreateView");
            mRootView = inflater.inflate(R.layout.fragment_b, container, false);
            return mRootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.i(TAG, "onActivityCreated");
            mBtn = (Button) mRootView.findViewById(R.id.btn);
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * FragmentB﹕ onDestroyView
                     * FragmentA﹕ onCreateView
                     * FragmentA﹕ onActivityCreated
                     * FragmentA﹕ savedInstanceState == null
                     */
                    getActivity().getSupportFragmentManager().popBackStack();//相當於按Back鍵
                }
            });
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            Log.i(TAG, "onSaveInstanceState");
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            Log.i(TAG, "onDestroyView");
        }
    }
}
