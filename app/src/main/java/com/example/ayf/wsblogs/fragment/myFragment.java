package com.example.ayf.wsblogs.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.ayf.wsblogs.MyDraftListActuvity;
import com.example.ayf.wsblogs.R;
import com.example.ayf.wsblogs.UploadPhoto;
import com.example.ayf.wsblogs.unify.User;

import java.time.Instant;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by asus on 2018/12/13.
 */

public class myFragment extends Fragment {
    Context context;
    private TextView myDrafts;
    private ImageView headbj,headImg;
    private TextView username,userVal;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my,container,false);
        context = getActivity();
        headbj = (ImageView)v.findViewById(R.id.h_back);
        myDrafts = (TextView)v.findViewById(R.id.my_drafts);
        username = (TextView)v.findViewById(R.id.user_name);
        userVal = (TextView)v.findViewById(R.id.user_val);
        headImg =(ImageView)v.findViewById(R.id.author_head_img);
        userVal.setText(User.getUserPhone());
        username.setText(User.getUsername());
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),UploadPhoto.class));
            }
        });
        myDrafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MyDraftListActuvity.class));
            }
        });
        Glide.with(this).load(R.drawable.image_head).bitmapTransform(new BlurTransformation(getActivity(), 10)).into(headbj);//高斯模糊
        return  v;
    }
}
