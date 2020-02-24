package com.example.ayf.wsblogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.ayf.wsblogs.fragment.addFragment;
import com.example.ayf.wsblogs.fragment.indexFragment;
import com.example.ayf.wsblogs.fragment.myFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.TabSelectionInterceptor;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private BottomBar bottomBar;
    private List<Fragment> fragmentList;


    JSONObject jsonObject;

    private BottomBarTab nearby;
    int state;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Thread(new Runnable() {
//
//            Unify unify = new Unify();
////            String url = unify.URL + "/index_api.php";
//            @Override
//            public void run() {
//                try {
//                    String jsonData = unify.request("http://api.tianapi.com/it/","key=0271191a3d0bcd8483debff0c759f20a&num=10");
//                    JSONArray jsonArray = new JSONArray(jsonData);
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject value = null;
//                        try {
//                            value = jsonArray.getJSONObject(i);
//                            String title = value.getString("title");
//                            String ctime = value.getString("ctime");
//                            String picUrl = value.getString("picUrl");
//                            String url = value.getString("url");
//                            Log.d("jpk", title);
//                            Log.d("jpk", ctime);
//                            Log.d("jpk", picUrl);
//                            Log.d("jpk", url);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        //获取到title值
//                    }
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        }).start();







        ButterKnife.bind(this);


        initViewPager();
        Intent intent=getIntent();
        state = intent.getIntExtra("state",2);

        if (state ==1){
            username = getIntent().getExtras().getString("username");

        }
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_index:
                        // 选择指定 id 的标签
                        nearby = bottomBar.getTabWithId(R.id.tab_myBlog);
                        nearby.setBadgeCount(5);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_add:
                        if (state == 0) {
                            Toast.makeText(MainActivity.this, "您还未登陆，请先登录！", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(1);
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, RcihEditorActivity.class);
                            startActivity(intent);
                        }

                        break;
                    case R.id.tab_myBlog:
                        if (state == 0) {
                            Toast.makeText(MainActivity.this, "您还未登陆，请先登录！", Toast.LENGTH_SHORT).show();

                        }
                        viewPager.setCurrentItem(2);
                        break;
                }
//                if (tabId == R.id.tab_index) {
//                    // 选择指定 id 的标签
//                    nearby = bottomBar.getTabWithId(R.id.tab_myBlog);
//                    nearby.setBadgeCount(5);
//                }
//                else if (tabId == R.id.tab_add){
//                    if (state == 0){
//                        Toast.makeText(MainActivity.this, "您还未登陆，请先登录！", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Intent intent = new Intent();
//                        intent.putExtra("username",username);
//                        intent.setClass(MainActivity.this,RcihEditorActivity.class);
//                        startActivity(intent);
//                    }
//
//                        // 选择指定 id 的标签
//                        nearby = bottomBar.getTabWithId(R.id.tab_index);
//                        nearby.setBadgeCount(5);
//
//                }
//                else if (tabId == R.id.tab_myBlog){
//                    if (state == 0){
//                        Toast.makeText(MainActivity.this, "您还未登陆，请先登录！", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_index) {
                    // 已经选择了这个标签，又点击一次。即重选。
                    nearby.removeBadge();
                }
            }
        });

        bottomBar.setTabSelectionInterceptor(new TabSelectionInterceptor() {
            @Override
            public boolean shouldInterceptTabSelection(@IdRes int oldTabId, @IdRes int newTabId) {
                // 点击无效
//                if (newTabId == R.id.tab_index ) {
//                    // ......
//                    // 返回 true 。代表：这里我处理了，你不用管了。
//                    return true;
//                }

                return false;
            }
        });
    }


    private void initViewPager() {


        fragmentList = new ArrayList<>();
        fragmentList.add(new indexFragment());
        fragmentList.add(new addFragment());
        fragmentList.add(new myFragment());


        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //viewPager 禁止滑动
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;

            }
        });
    }

}