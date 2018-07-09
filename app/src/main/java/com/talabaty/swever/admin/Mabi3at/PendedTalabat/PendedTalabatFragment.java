package com.talabaty.swever.admin.Mabi3at.PendedTalabat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talabaty.swever.admin.R;

public class PendedTalabatFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pend_talabat,container,false);
        return view;
    }
}
