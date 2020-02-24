package com.example.ayf.wsblogs.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ayf.wsblogs.R;
import com.example.ayf.wsblogs.WebViewAcitivity;
import com.example.ayf.wsblogs.unify.Unify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/12/14.
 */

public class fragment1 extends Fragment {

    private ListView lv;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> lst;
    RequestQueue requestQueue;//1217
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);
        lv = (ListView) view.findViewById(R.id.tab_listview);    //实例化
        return view;
    }

    @Override
    public  void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lst = Collections.synchronizedList(new ArrayList<Map<String, Object>>());
        getData();
        //线程不等待 数据会出不来
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    //数据的获取@！
    private void getData() {
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(getActivity());
        //创建一个请求
        String url = "http://api.tianapi.com/it/?key=f7f95f1e363f027df4635a1a1277a0a8&num=10";

        JsonObjectRequest request =new  JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            //正确接收数据回调
            @Override
            public void onResponse(JSONObject s) {
//                s = s.substring(39);
//
//                s = s.substring(s.indexOf("["),s.indexOf("]")+1);
                Log.d("jpk", ""+s);
                try {
                    JSONArray jsonArray = s.getJSONArray("newslist");
                    Log.d("jpk", ""+jsonArray);
                    Map<String, Object> map = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject value = null;
                        map = new HashMap<String, Object>();
                        value = jsonArray.getJSONObject(i);
                        String title = value.getString("title");
                        String ctime = value.getString("ctime");
                        String picUrl = value.getString("picUrl");
                        String description = value.getString("description");
                        String url = value.getString("url");
                        map.put("title",title);
                        map.put("body", ctime);
                        map.put("img", picUrl);
                        map.put("description",description);
                        map.put("url",url);
                        lst.add(map);
                        //获取到title值
                    }
                    adapter = new SimpleAdapter(getActivity(), lst, R.layout.tab_listview_item,
                            new String[]{ "title", "body","description"},
                            new int[]{R.id.itemtitle, R.id.itembody,R.id.item_user});//配置适配器，并获取对应Item中的ID
                    adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

                        public boolean setViewValue(View view, Object data,
                                                    String textRepresentation) {
                            //判断是否为我们要处理的对象
                            if (view instanceof ImageView && data instanceof Bitmap) {
                                ImageView iv = (ImageView) view;

                                iv.setImageBitmap((Bitmap) data);
                                return true;
                            } else
                                return false;
                        }
                    });
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent();
                            Map<String,Object> map = lst.get(position);
                            intent.putExtra("url",map.get("url").toString());//改！！！！！
                            intent.setClass(getActivity(), WebViewAcitivity.class);//改！！！！！
                            startActivity(intent);

                        }
                    });
                    lv.setAdapter(adapter);
                    // }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("jpk",""+e);
                }

            }
        }, new Response.ErrorListener() {//异常后的监听数据
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volley_result.setText("加载错误"+volleyError);
            }
        });
        //将get请求添加到队列中
        requestQueue.add(request);

//        new Thread(new Runnable() {
//            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//            Unify unify = new Unify();
//
//            //            String url = unify.URL + "/index_api.php";
//            @Override
//            public void run() {
//                try {
//                    String jsonData = unify.request("http://api.tianapi.com/it/", "key=f7f95f1e363f027df4635a1a1277a0a8&num=10");
//                    JSONArray jsonArray = new JSONArray(jsonData);
//                    Map<String, Object> map = null;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject value = null;
//                        map = new HashMap<String, Object>();
//                        value = jsonArray.getJSONObject(i);
//                        String title = value.getString("title");
//                        String ctime = value.getString("ctime");
//                        String picUrl = value.getString("picUrl");
//                        String url = value.getString("url");
//                        map.put("title",title);
//                        map.put("body", ctime);
//                        map.put("img", getBitmap(picUrl));
//                        map.put("url",url);
//                        lst.add(map);
//
//
//                        //获取到title值
//                    }
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        }).start();
    }
//将需要的值传入map中
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("title", "软院最新公告事项");
//        map.put("body", "不知道未来几天有什么最新消息？那就点我查看查看呗");
//        map.put("img", R.drawable.app_logo);
//        list.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("title", "校内最新消息通知");
//        map.put("body", "校级活动，化工电影本周放啥？艺设妹子有什么动向？点我查看");
//        map.put("img", R.drawable.app_logo);
//        list.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("title", "圈内交流园地");
//        map.put("body", "来都来了，何不进来说几句？");
//        map.put("img", R.drawable.app_logo);
//        list.add(map);

    public Bitmap getBitmap(String imageUrl) {
        Bitmap mBitmap = null;
        try {
//            ImageRequest imageRequest = new ImageRequest(
//                    imageUrl,
//                    new Response.Listener<Bitmap>() {
//                        @Override
//                        public void onResponse(Bitmap response) {
//
//                        }
//                    }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            });

            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mBitmap;
    }





}
