package com.srmn.xwork.androidlib.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;


/**
 * Created by wyouflf on 15/11/4.
 */
public class BaseFragment extends Fragment {

    protected Context parentActivity = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = this.getActivity();
    }



}
