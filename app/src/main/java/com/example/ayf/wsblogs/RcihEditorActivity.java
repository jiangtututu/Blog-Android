package com.example.ayf.wsblogs;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayf.wsblogs.SQLite.DBHelper;
import com.example.ayf.wsblogs.unify.PopupWindowUtil;
import com.example.ayf.wsblogs.unify.RealPathFromUriUtils;
import com.example.ayf.wsblogs.unify.Unify;
import com.example.ayf.wsblogs.unify.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.richeditor.RichEditor;

public class RcihEditorActivity extends AppCompatActivity {
    private static String TAG = "ayf";
    private RichEditor mEditor;
    private TextView mPreview;
    private EditText title;
    private ImageView imageView;
    private LinearLayout mLyH;
    private TextView mTvH;
    private TextView mTvA;
    private ImageView mImgPalette;
    private LinearLayout mLyMark;
    private Button mUpLoad, mSave;
    private SQLiteDatabase db;
    private ImageButton btnBack;

    String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int REQUEST_PICK_IMAGE = 11101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.richedior);
        mEditor = (RichEditor) findViewById(R.id.editor);
        imageView = (ImageView) findViewById(R.id.img_inset);
        mTvH = (TextView) findViewById(R.id.tv_h);
        mLyH = findViewById(R.id.ly_h);
        mTvA = findViewById(R.id.tv_a);
        mLyMark = (LinearLayout) findViewById(R.id.ly_mark);
        mImgPalette = (ImageView) findViewById(R.id.img_palette);
        mUpLoad = (Button) findViewById(R.id.uploading);
        title = (EditText) findViewById(R.id.tv_title);
        mSave = (Button) findViewById(R.id.save);
        btnBack = (ImageButton)findViewById(R.id.btn_back);

        final View mViewStyle = View.inflate(RcihEditorActivity.this, R.layout.item_popup_style, null);
        final View mViewTypeface = View.inflate(RcihEditorActivity.this, R.layout.item_popup_typeface, null);
        final View mViewColor = View.inflate(RcihEditorActivity.this, R.layout.item_popup_color, null);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //保存文章到本地
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTitle = title.getText().toString().trim();
                int userID = User.getUserID();
                String msg = mEditor.getHtml();
                Date date = new Date();
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                String myTime = df1.format(date);
                Log.d(TAG, "onClick: 时间是：" + df1.format(date));
                if (!"".equals(sTitle)) {
                    ContentValues cv = new ContentValues();
                    try {
                        cv.put("article_title", sTitle);
                        cv.put("user_id", userID);
                        cv.put("msg", msg);
                        cv.put("time", myTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    db = new DBHelper(RcihEditorActivity.this).getWritableDatabase();
                    try {
                        long a = db.insert(DBHelper.TABLE_NAME, null, cv);
                        if (a != -1){
                            Toast.makeText(RcihEditorActivity.this,"保存成功",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(RcihEditorActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //上传按钮监听
        mUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    Unify unify = new Unify();
                    String url = unify.URL + "/upLoad.php";
                    String username = User.getUsername();

                    @Override
                    public void run() {
                        try {
                            String params = "username=" + username
                                    + '&' + "html=" + mEditor.getHtml()
                                    + '&' + "title=" + title.getText().toString().trim()
                                    + '&' + "flag=1";//1表示增加一条记录
                            int result = unify.linkPHP(url, params);
                            if (result == 1) {
                                Looper.prepare();
                                Toast.makeText(RcihEditorActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                Looper.prepare();
                                Toast.makeText(RcihEditorActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        mTvA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: 点击A选择字体样式");
                PopupWindowUtil.showPopupWindow(RcihEditorActivity.this, mViewStyle, view, 1, itemsOnClick);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.focusEditor();
                ActivityCompat.requestPermissions(RcihEditorActivity.this, mPermissionList, 100);
            }
        });
        mTvH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindowUtil.showPopupWindow(RcihEditorActivity.this, mViewTypeface, view, 1, itemsOnClick);
            }
        });
        mImgPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindowUtil.showPopupWindow(RcihEditorActivity.this, mViewColor, view, 1, itemsOnClick);
            }
        });
        mLyMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.setBlockquote();
            }
        });
        //初始化编辑高度
        // mEditor.setEditorHeight(200);
        //初始化字体大小
        mEditor.setEditorFontSize(22);
        //初始化字体颜色
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);

        //初始化内边距
        mEditor.setPadding(10, 10, 10, 10);
        //设置编辑框背景，可以是网络图片
        // mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        // mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        //设置默认显示语句
        mEditor.setPlaceholder("创作你的创作……");
        //设置编辑器是否可用
        mEditor.setInputEnabled(true);

        mPreview = (TextView) findViewById(R.id.preview);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                mPreview.setText(text);
            }
        });

        // 撤销当前标签状态下所有内容
        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        init();
    }


    //获取要编辑的保存的文章
    private void init() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            db = new DBHelper(RcihEditorActivity.this).getReadableDatabase();
            Cursor c = db.rawQuery("select * from " + DBHelper.TABLE_NAME + " where _id =" + bundle.getInt("articleID"), null);
            c.moveToFirst();
            String ordTitle = c.getString(4);
            String ordMsg = c.getString(2);
            String ordTime = c.getString(3);
            title.setText(ordTitle);
            mEditor.setHtml(ordMsg);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean writeExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && writeExternalStorage && readExternalStorage) {
                    getImage();
                } else {
                    Toast.makeText(this, "请设置必要权限", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void getImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_PICK_IMAGE);
        } else {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("image/*");
//            startActivityForResult(intent, REQUEST_PICK_IMAGE);
            /*打开相册*/
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 10);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            if (data != null) {
                String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
//                mEditor.insertImage("https://unsplash.it/2000/2000?random&58",
//                        "huangxiaoguo\" style=\"max-width:100%");
                mEditor.insertImage(realPathFromUri, realPathFromUri + "\" style=\"max-width:100%");
//                        mEditor.insertImage(realPathFromUri, realPathFromUri + "\" style=\"max-width:100%;max-height:100%");

            } else {
                Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_b:
                    Log.d(TAG, "onClick: 加粗");
                    mEditor.focusEditor();
                    mEditor.setBold();
//                PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.img_t:
                    Log.d(TAG, "onClick: 斜体");
                    mEditor.focusEditor();
                    mEditor.setItalic();
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.img_u:
                    Log.d(TAG, "onClick: 下划线");
                    mEditor.focusEditor();
                    mEditor.setUnderline();
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.ly_point:
                    Log.d(TAG, "onClick: Bullets");
                    mEditor.setBullets();
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.tv_l:
                    mEditor.focusEditor();
                    mEditor.setStrikeThrough();
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.img_h1:
                    mEditor.setHeading(1);
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.img_h2:
                    mEditor.setHeading(2);
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.img_h3:
                    mEditor.setHeading(3);
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.img_zw:
                    mEditor.setHeading(3);
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.ly_red:
                    mEditor.setTextColor(Color.RED);
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.ly_orange:
                    mEditor.setTextColor(getResources().getColor(R.color.orange));
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.ly_yellow:
                    mEditor.setTextColor(Color.YELLOW);
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.ly_violet:
                    mEditor.setTextColor(getResources().getColor(R.color.violet));
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.ly_blue:
                    mEditor.setTextColor(getResources().getColor(R.color.blue));
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.ly_black:
                    mEditor.setTextColor(getResources().getColor(R.color.black));
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                case R.id.ly_white:
                    mEditor.setTextColor(getResources().getColor(R.color.white));
                    PopupWindowUtil.popupWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
}


