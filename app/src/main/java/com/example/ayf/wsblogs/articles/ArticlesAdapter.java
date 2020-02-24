package com.example.ayf.wsblogs.articles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class ArticlesAdapter extends ArrayAdapter {
    private final int resourceId;
    public ArticlesAdapter(@NonNull Context context, int resource, List<ArticlesData> object) {
        super(context, resource,object);
        this.resourceId = resource;

    }

    //未完成
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ArticlesData articlesData = (ArticlesData)getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
//        TextView articleID = view.findViewById(R.id.article_id);
        return view;
    }
}
