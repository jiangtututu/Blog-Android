package com.example.ayf.wsblogs.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayf.wsblogs.LoginAcitivity;
import com.example.ayf.wsblogs.R;

/**
 * Created by asus on 2018/12/13.
 */

public class addFragment extends Fragment {
    private TextView textView;
    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add,container,false);
        context = getActivity();
        textView=(TextView)v.findViewById(R.id.add_title);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LoginAcitivity.class));
            }
        });
        return  v;
    }
}
