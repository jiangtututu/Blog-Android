package com.example.ayf.wsblogs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.ayf.wsblogs.comments.CommentsAdapter;
import com.example.ayf.wsblogs.comments.CommentsData;
import com.example.ayf.wsblogs.unify.MyListView;
import com.example.ayf.wsblogs.unify.Unify;
import com.example.ayf.wsblogs.unify.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowBlogActivity extends AppCompatActivity {

    private TextView title, author, motto, time,count_praise;
    private ImageView headImg;   //头像
    private WebView webView;
    private Handler h;
    private ScrollView mScrollView;
    private MyListView listView;
    private List<CommentsData> commentsList = new ArrayList<CommentsData>();
    private EditText myComments;
    private Button release,focus;
    private ImageButton praiseBtn;
    private int articleID;
    private boolean ispraise=false,isFocus=false,isChangFocus=false,isChangePraise = false;
    private String authorName;
    @SuppressLint({"HandlerLeak", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_blog);
        title = (TextView) findViewById(R.id.article_title);
        author = (TextView) findViewById(R.id.author);
        webView = (WebView) findViewById(R.id.article_area);
        time = (TextView)findViewById(R.id.article_info1);
        count_praise = (TextView)findViewById(R.id.count_praise);
        mScrollView = (ScrollView)findViewById(R.id.sro_vw);
        listView = (MyListView) findViewById(R.id.comments_list);
        myComments = (EditText)findViewById(R.id.my_comments);
        release = (Button)findViewById(R.id.release);
        praiseBtn = (ImageButton)findViewById(R.id.btn_praise);
        focus = (Button)findViewById(R.id.focus);

        //发布评论按钮
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String myC = myComments.getText().toString().trim();//获取评论内容
                        String replyer = User.getUsername();
                        Unify unify = new Unify();
                        String url = unify.URL+"/addComments.php";
                        String params = "msg=" + myC
                                + '&' + "replyer=" +replyer
                                + '&' + "articleID=" + articleID;

                        Log.d("ayfaa", "run: "+params);
                        try {
                            int result = unify.linkPHP(url,params);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        //点击关注  取消关注
        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFocus = !isFocus;
                isChangFocus = !isChangFocus;
                isFocused();
            }
        });

        //文章点赞 取消点赞
        praiseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ispraise = !ispraise;
                isChangePraise = !isChangePraise;
                isPraised();
                changePraiseCount();
            }
        });

        init();
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {//handler 获取发送的消息
                super.handleMessage(msg);
                title.setText(msg.getData().getString("title"));
                author.setText(msg.getData().getString("username"));
                webView.loadDataWithBaseURL(null,msg.getData().getString("msg"), "text/html", "utf-8", null);
                time.setText("发布于：" + msg.getData().getString("time"));
                count_praise.setText(msg.getData().getString("count_praise"));
                articleID = msg.getData().getInt("articleID");
            }
        };
        //listView添加适配器 commentsList 中有init时添加的每个item的数据
        CommentsAdapter adapter = new CommentsAdapter(ShowBlogActivity.this,R.layout.comments_list_item,commentsList);
        listView.setAdapter(adapter);
    }
    private void isFocused(){
        if (isFocus){
            focus.setBackgroundResource(R.drawable.light_gray_bj);
            focus.setText(R.string.focused);
        }else{
            focus.setBackgroundResource(R.drawable.darkblue_border_radius);
            focus.setText(R.string.focus);
        }
    }

    private void isPraised(){
        if (ispraise){
            praiseBtn.setImageResource(R.drawable.praised);
        }else{
            praiseBtn.setImageResource(R.drawable.praise);
        }
    }

    private void changePraiseCount(){
        if (ispraise){
            String i = count_praise.getText().toString();
            int count = Integer.parseInt(i)+1;
            count_praise.setText(String.valueOf(count));

        }else{
            String i = count_praise.getText().toString();
            int count = Integer.parseInt(i)-1;
            count_praise.setText(String.valueOf(count));
        }
    }

    //获取文章以及对该文章的所有评论
    public void init() {
        new Thread(new Runnable() {
            //获取文章
            Unify unify = new Unify();
            String url = unify.URL + "/Getarticles.php";
            JSONObject jsonObject;
            Intent intent = getIntent();
            @Override
            public void run() {
                try
                {

                    String params = "type="+intent.getIntExtra("id",0);//11是要查看的文章的编号 从首页点击list传入的文章id
                    jsonObject = unify.getArticle(url, params);

                    //由于子线程中不能直接设置更改activity主线程中各个控件的值 所以需要通过 Handler 发送消息 将数据存放至一个bundle 再将消息传给Handler的handleMessage方法去获取
                    Message message = Message.obtain();//实例化一个消息
                    Bundle bundle = new Bundle();//实例化一个bundle 并将数据存入此bundle
                    bundle.putString("title",jsonObject.getString("title"));
                    bundle.putString("username",jsonObject.getString("username"));
                    bundle.putString("msg",jsonObject.getString("msg"));
                    bundle.putString("time",jsonObject.getString("upload_time"));
                    bundle.putString("count_praise",jsonObject.getString("starts"));
                    bundle.putInt("articleID",jsonObject.getInt("id"));//文章id 用于 评论时插入数据（article_id）和 获取对应文章的评论使用
                    message.setData(bundle);//bundle存入消息
                    h.sendMessage(message);//发送消息  可由Handler的handleMessage方法去获取
                    authorName = jsonObject.getString("username");

                } catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e){
                    e.printStackTrace();
                    Log.d("ayfaa", "run: jason失败");
                }

                //查看该作者是否已经关注
                try {
                    String urlIsFocus = unify.URL+"/isFocus.php";
                    String paramsIsFocus = "authorName=" + jsonObject.getString("username")
                            +"&userName="+User.getUsername();
                    Unify unify = new Unify();
                    int result = Integer.parseInt(unify.link(urlIsFocus,paramsIsFocus));
                    Log.d("ayfaa", "userName: "+User.getUsername()+"   "+result);
                    if (result != 0){
                        isFocus = true;
                    }
                    isFocused();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //查看该文章是否已经点赞
                try {
                    Unify unify =new Unify();
                    String urlPraise = unify.URL + "/praise.php";
                    String paramsPraise =  "articleID=" + jsonObject.getInt("id")
                            + "&userID=" + User.getUserID();
                    int result2 = Integer.parseInt(unify.link(urlPraise,paramsPraise));
                    if (result2 != 0){
                        ispraise = true;
                    }
                    isPraised();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //获取该文章所有评论
                String url = unify.URL+"/getComments.php";
                try {
                    String params = "articleID=" + jsonObject.getInt("id");
                    Log.d("ayfaa", "run:params "+params);
                    JSONArray jsonArray1 = unify.getComments(url,params);//返回一个JSONArray
                    if (jsonArray1!=null){
                        for (int i=0;i<jsonArray1.length();i++){//遍历JSONArray 将里面的元素（每一条评论信息）添加到commentsList
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = jsonArray1.getJSONObject(i);
                                CommentsData commentsData = new CommentsData(jsonObject.getString("replyer"),null,jsonObject.getString("msg"),jsonObject.getString("time_reply"),5);
                                commentsList.add(commentsData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy(){
        Log.d("ayfaa", "onDestroy: "+articleID);
        new Thread(new Runnable(){
            @Override
            public void run() {
                if (isChangePraise){
                    Unify unify =new Unify();
                    String url = unify.URL + "/praise.php";
                    String params =  "articleID=" + articleID
                            + "&userID=" + User.getUserID()
                            + "&type=1" ;//1表示对文章点赞
                    try {
                        unify.praiseArticle(url,params);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (isChangFocus){
                    Log.d("ayfaa", "isChangFocus: "+isChangFocus);
                    Unify unify = new Unify();
                    String url = unify.URL+"/isFocus.php";
                    String params = "authorName=" + authorName
                            +"&userName="+User.getUsername()
                            +"&type=1";
                    try {
                        unify.FocusOrCancel(url,params);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        super.onDestroy();
    }
}
