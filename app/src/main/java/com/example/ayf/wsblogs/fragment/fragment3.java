package com.example.ayf.wsblogs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ayf.wsblogs.R;
import com.example.ayf.wsblogs.ShowBlogActivity;
import com.example.ayf.wsblogs.unify.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/12/14.
 */

public class fragment3 extends Fragment {
    private ListView lv;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> lst;
    RequestQueue requestQueue;//1217

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third,container,false);
        lv = (ListView) view.findViewById(R.id.tab_listview3);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();

    }

    @Override
    public  void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lst = Collections.synchronizedList(new ArrayList<Map<String, Object>>());
    }
    //数据的获取@！
    private void getData() {
        //创建一个请求队列
        requestQueue = Volley.newRequestQueue(getActivity());
        //创建一个请求
        String url = "http://47.110.130.207/WSBlogs/Getarticles.php?type=3";
        url = url + "&username=" + User.getUsername();

        Log.d("ayfaa", "getData: "+url);

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            //正确接收数据回调
            @Override
            public void onResponse(JSONObject s) {
                try {
                    JSONArray jsonArray = s.getJSONArray("data");
                    Log.d("jpk", jsonArray.length()+"  " + jsonArray);
                    Map<String, Object> map = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject value = null;
                        map = new HashMap<String, Object>();
                        value = jsonArray.getJSONObject(i);
                        String title = value.getString("title");
                        String ctime = value.getString("upload_time");
                        String username = value.getString("username");
                        int id = value.getInt("id");
                        map.put("title", title);
                        map.put("body", ctime);
                        map.put("username", username);
                        map.put("id", id);
                        lst.add(map);
                        //获取到title值
                    }
                    adapter = new SimpleAdapter(getActivity(), lst, R.layout.tab_listview_item,
                            new String[]{"title", "body", "username"},
                            new int[]{R.id.itemtitle, R.id.itembody, R.id.item_user});//配置适配器，并获取对应Item中的ID

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent();
                            Map<String, Object> map = lst.get(position);
                            intent.putExtra("id", Integer.parseInt(map.get("id").toString()));//改！！！！！
                            Log.d("jpk", "" + Integer.parseInt(map.get("id").toString()));
                            intent.setClass(getActivity(), ShowBlogActivity.class);//改！！！！！
                            startActivity(intent);

                        }
                    });
                    lv.setAdapter(adapter);
                    // }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("jpk", "" + e);
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
    }
}
