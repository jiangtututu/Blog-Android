package com.example.ayf.wsblogs.unify;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Unify {

    public final String URL = "http://47.110.130.207:80/WSBlogs";//ip会改变 每次需要修改

    //连接服务器后端
    public String link(String urlstr,String params)throws IOException {
        URL url = new URL(urlstr);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setDoOutput(true);
        http.setRequestMethod("POST");
        OutputStream out = http.getOutputStream();//url 连接失败 此处结束application
        out.write(params.getBytes());//post提交参数
        out.flush();
        out.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));//获得输入流
        String line = "";
        StringBuilder sb = new StringBuilder();//建立输入缓冲区
        while (null != (line = bufferedReader.readLine())) {//结束会读入一个null值
            sb.append(line);//写缓冲区
        }
        String result = sb.toString();//返回结果
        return result;
    }

    public int linkPHP(String urlstr,String params) throws IOException {
        int returnResult =0;
        String result = link(urlstr,params);
        try {
        /*获取服务器返回的JSON数据*/
            JSONObject jsonObject = new JSONObject(result);
            returnResult = jsonObject.getInt("status");//获取JSON数据中status字段值
            Log.d("statys",""+returnResult);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data " + e.toString());
        }
        Log.d("returnResult",""+returnResult);
        return returnResult;
    }

    //登录返回数据 是否成功登录，成功登录返回用户信息
    public JSONObject getUser (String urlstr,String params) throws IOException {
        String result = link(urlstr,params);
        JSONObject jsonObject = new JSONObject();
        try {
            /*获取服务器返回的JSON数据*/
            jsonObject = new JSONObject(result);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data " + e.toString());
        }
        return jsonObject;
    }

    //获取文章 通过文章ID
    public JSONObject getArticle(String urlstr,String params)throws IOException{
        JSONObject jsonObject = new JSONObject();
        String result = link(urlstr,params);
        try {
            /*获取服务器返回的JSON数据*/
            jsonObject = new JSONObject(result);
            Log.d("ayfaa", "getArticle: "+result);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data " + e.toString());
        }
        return jsonObject;
    }


    //获取文章评论 通过文章ID
    public JSONArray getComments(String urlstr,String params)throws IOException{
        JSONArray jsonArray = new JSONArray();
        String result = link(urlstr,params);
        try {
            /*获取服务器返回的JSON数据*/
            jsonArray = new JSONArray(result);
            Log.d("ayfaa", "getComments: "+result);
            Log.d("ayfaa", "getComments: "+jsonArray);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data " + e.toString());
        }
        return jsonArray;
    }


    //文章点赞 取消点赞
    public void praiseArticle(String urlstr,String params)throws IOException{
        String result = link(urlstr,params);
    }

    //关注 取消关注
    public void FocusOrCancel(String urlstr,String params)throws IOException{
         String res = link(urlstr,params);
    }

    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();

            result = sbf.substring(39);

            result = result.substring(result.indexOf("["),result.indexOf("]")+1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
