package com.talabaty.swever.admin.Mabi3at;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.talabaty.swever.admin.R;

public class MainHome extends Fragment {
    Button new_talabat, ready_talabat, pended_tasks, returned_talabat, notification_firend, done_talabat, rejected_report, saled_report;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mabi3at,container,false);
        new_talabat = (Button) view.findViewById(R.id.new_talabat);
        ready_talabat = view.findViewById(R.id.ready_talabat);
        pended_tasks = view.findViewById(R.id.pend_tasks);
        returned_talabat = view.findViewById(R.id.returned_talabat);
        notification_firend = view.findViewById(R.id.notification_friend);
        done_talabat = view.findViewById(R.id.done_talabat);
        rejected_report = view.findViewById(R.id.rejected_talabat_report);
        saled_report = view.findViewById(R.id.saled_talabat_report);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Intent intent = new Intent(getActivity(), Mabi3atNavigator.class);
        new_talabat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment","new");
                startActivity(intent);
            }
        });

        ready_talabat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment","ready");
                startActivity(intent);
            }
        });

        pended_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment","pend");
                startActivity(intent);
            }
        });

        returned_talabat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment","returned");
                startActivity(intent);
            }
        });


        notification_firend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment", "notification");
                startActivity(intent);
            }
        });

        done_talabat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment", "done");
                startActivity(intent);
            }
        });

        rejected_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment", "rejected");
                startActivity(intent);
            }
        });

        saled_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment", "sailed");
                startActivity(intent);
            }
        });
    }
}
