package com.example.ayf.wsblogs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.ayf.wsblogs.SQLite.DBHelper;
import com.example.ayf.wsblogs.drafts.DraftsAdapter;
import com.example.ayf.wsblogs.drafts.DraftsData;
import com.example.ayf.wsblogs.unify.User;
import java.util.ArrayList;
import java.util.List;

public class MyDraftListActuvity extends AppCompatActivity {
    private ListView listView;
    private ImageView back;
    private TextView draftTitle, countDrafts;
    private SQLiteDatabase db;
    private List<DraftsData> draftsDataList = new ArrayList<DraftsData>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydrafts);

        listView = (ListView) findViewById(R.id.draft_list);
        back = (ImageView) findViewById(R.id.back);
        countDrafts = (TextView) findViewById(R.id.count_drafts);

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_list_draft, menu);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long id = info.id;
        int a = draftsDataList.get((int) id).getId();
        String[] args = {String.valueOf(a)};
        switch (item.getItemId()) {
            case R.id.edit_menu:
                Intent intent = new Intent();
                intent.putExtra("articleID",a);
                intent.setClass(MyDraftListActuvity.this,RcihEditorActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_menu:
                db.delete(DBHelper.TABLE_NAME,"_id=?",args);//删除sqlite中相应的文章
                refresh();//刷新页面 重新获取数据
                break;
            case R.id.delete_all_menu:
                break;
            default:
        }
        return super.onContextItemSelected(item);
    }

    public void init() {
        db = new DBHelper(MyDraftListActuvity.this).getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + DBHelper.TABLE_NAME + " where user_id = " + User.getUserID(), null);
        while (c.moveToNext()) {
            Log.d("ayfaaa", "init: " + c.getString(3) + "  " + c.getString(2) + "  " + c.getString(4));
            DraftsData draftsData = new DraftsData(c.getString(4), c.getString(2), c.getString(3), c.getInt(0));
            draftsDataList.add(draftsData);
        }
        DraftsAdapter adapter = new DraftsAdapter(MyDraftListActuvity.this, R.layout.drafts_liat_item, draftsDataList);
        listView.setAdapter(adapter);
    }

    private void refresh() {
        finish();
        Intent intent = new Intent(MyDraftListActuvity.this, MyDraftListActuvity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        finish();
        Intent intent = new Intent(MyDraftListActuvity.this, MyDraftListActuvity.class);
        startActivity(intent);
        super.onRestart();
    }
}
