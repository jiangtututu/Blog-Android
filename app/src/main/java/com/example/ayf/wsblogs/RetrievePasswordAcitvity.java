package com.example.ayf.wsblogs;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ayf.wsblogs.unify.Unify;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

//找回密码
public class RetrievePasswordAcitvity extends AppCompatActivity implements View.OnClickListener {
    private EditText newPwd,newPwdAgain,phoneNum,code;
    private Button sent_code;
    private ImageView backImg;
    public EventHandler eh2;//事件接收器
    private RetrievePasswordAcitvity.TimeCount myTimeCount;//计时器
    private Unify unify;
    private int result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieve_password);
        newPwd = (EditText)findViewById(R.id.new_pwd);
        newPwdAgain = (EditText)findViewById(R.id.new_pwd_agian);
        phoneNum = (EditText)findViewById(R.id.phone_num);
        code = (EditText)findViewById(R.id.your_code);
        findViewById(R.id.done_btn).setOnClickListener(this);
        sent_code = (Button)findViewById(R.id.sent_code);
        sent_code.setOnClickListener(this);
        backImg = (ImageView)findViewById(R.id.back);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ayf", "onClick: 返回到登录页");
                finish();
            }
        });
        myTimeCount = new TimeCount(60000, 1000);
        init();
    }

    //点击监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sent_code:
                Log.d("ayf", "onClick:点击发送验证码 ");
                if (checkTel(phoneNum.getText().toString().trim())) {
                    SMSSDK.getVerificationCode("+86", phoneNum.getText().toString());//发送验证码
                    Toast.makeText(RetrievePasswordAcitvity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    myTimeCount.start();
                } else {
                    Toast.makeText(RetrievePasswordAcitvity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.done_btn:
                Log.d("ayf", "onClick:点击完成 ");
                if (newPwd.getText().toString().trim().equals(newPwdAgain.getText().toString().trim())){//判断密码输入是否正确
                    if (newPwd.getText().toString().trim()==""){
                        Toast.makeText(RetrievePasswordAcitvity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    }else{
                        SMSSDK.submitVerificationCode("+86",phoneNum.getText().toString().trim(),code.getText().toString().trim());//提交验证                    ;
                    }
                }else{
                    Log.d("ayf", "onClick: "+newPwd.getText().toString().trim()+","+newPwdAgain.getText().toString().trim());
                    Toast.makeText(RetrievePasswordAcitvity.this, "两次密码输入不相同，请重新输入", Toast.LENGTH_SHORT).show();
                    newPwd.setText("");//删除密码输入框内容
                    newPwdAgain.setText("");
                }
                break;
//            case R.id.back:
//                Log.d("ayf", "onClick: 返回到登录页");
//                RetrievePasswordAcitvity.this.finish();
//                startActivity(new Intent(RetrievePasswordAcitvity.this,LoginAcitivity.class));
//                break;
            default:
                break;
        }
    }

    //初始化事件接收器
    private void init() {
        eh2 = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                if (result == SMSSDK.RESULT_COMPLETE) {//回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                        //后台重置密码
                        unify = new Unify();
                        String url = unify.URL + "/updatePwd.php";
                        String parmas = "input_phone="+phoneNum.getText().toString().trim()+"&input_pwd="+newPwd.getText().toString().trim();
                        try {
                            result = unify.linkPHP(url,parmas);
                            if (result==1){
                                Looper.prepare();
                                Toast.makeText(RetrievePasswordAcitvity.this, "修改成功!去登陆", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RetrievePasswordAcitvity.this, LoginAcitivity.class));//页面跳转
                                Looper.loop();
                            }else{
                                Log.d("ayf", "点击完成:  " + result);
                                Looper.prepare();
                                Toast.makeText(RetrievePasswordAcitvity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                        Looper.prepare();
//                        Toast.makeText(RetrievePasswordAcitvity.this, "已重置密码", Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                        RetrievePasswordAcitvity.this.finish();
//                        startActivity(new Intent(RetrievePasswordAcitvity.this, LoginAcitivity.class)); //页面跳转
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) { //返回支持发送验证码的国家列表

                    }
                }else {
                    ((Throwable) data).printStackTrace();
                    Looper.prepare();
                    Toast.makeText(RetrievePasswordAcitvity.this, "验证码输入错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    myTimeCount.onFinish();
                }
            }
        };
        SMSSDK.registerEventHandler(eh2); //注册短信回调
    }

    //计时器
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            sent_code.setEnabled(false);
            sent_code.setText(l / 1000 + "秒后重发");
        }

        @Override
        public void onFinish() {
            sent_code.setEnabled(true);
            sent_code.setText("发送验证码");//计时结束后重新设定按钮值
        }
    }

    //正则匹配手机号码
    public boolean checkTel(String tel) {
        Pattern p = Pattern.compile("^[1][3,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh2);
    }
}
