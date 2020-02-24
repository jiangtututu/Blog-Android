package com.example.ayf.wsblogs.comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ayf.wsblogs.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class CommentsAdapter extends ArrayAdapter {
    private final int resourceId;
    public CommentsAdapter(@NonNull Context context, int resource, List<CommentsData> object) {
        super(context, resource,object);
        this.resourceId = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CommentsData commentsD = (CommentsData) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView author = view.findViewById(R.id.comment_author);
        RoundedImageView headImg = view.findViewById(R.id.author_head_img);
        TextView comments = view.findViewById(R.id.comments_msg);
        TextView commentsTime = view.findViewById(R.id.comments_time);

        author.setText(commentsD.getAuthor());
        comments.setText(commentsD.getComments());
        commentsTime.setText(commentsD.getCommentsTime());
        return view;
    }
}
