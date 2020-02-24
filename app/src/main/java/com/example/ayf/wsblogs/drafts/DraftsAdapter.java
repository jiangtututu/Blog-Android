package com.example.ayf.wsblogs.drafts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ayf.wsblogs.R;

import java.util.List;

public class DraftsAdapter extends ArrayAdapter {
    private final int resourceId;
    public DraftsAdapter(@NonNull Context context, int resource, List<DraftsData> draftsDataList) {
        super(context, resource,draftsDataList);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DraftsData draftsData = (DraftsData)getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView titlt = view.findViewById(R.id.title_draft_item);
        TextView msg = view.findViewById(R.id.msg_draft);
        TextView time = view.findViewById(R.id.time_draft);
        TextView id = view.findViewById(R.id.id_draft);

        titlt.setText(draftsData.getTitle());
        msg.setText(draftsData.getMsg());
        time.setText("保存于："+draftsData.getTime());
        id.setText(draftsData.getId()+"");
        return view;
    }
}
