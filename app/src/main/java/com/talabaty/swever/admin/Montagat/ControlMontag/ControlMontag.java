package com.talabaty.swever.admin.Montagat.ControlMontag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.talabaty.swever.admin.Home;
import com.talabaty.swever.admin.Mabi3at.Mabi3atNavigator;
import com.talabaty.swever.admin.R;

public class ControlMontag extends Fragment {
    
    ImageButton []edit, delete;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_montag,container,false);
        edit = new ImageButton[10];
        delete = new ImageButton[10];
        delete[0] = view.findViewById(R.id.delete1);
        edit[0] = view.findViewById(R.id.edit1);
        delete[1] = view.findViewById(R.id.delete2);
        edit[1] = view.findViewById(R.id.edit2);
        delete[2] = view.findViewById(R.id.delete3);
        edit[2] = view.findViewById(R.id.edit3);
        delete[3] = view.findViewById(R.id.delete4);
        edit[3] = view.findViewById(R.id.edit4);
        delete[4] = view.findViewById(R.id.delete5);
        edit[4] = view.findViewById(R.id.edit5);
        delete[5] = view.findViewById(R.id.delete6);
        edit[5] = view.findViewById(R.id.edit6);
        delete[6] = view.findViewById(R.id.delete7);
        edit[6] = view.findViewById(R.id.edit7);
        delete[7] = view.findViewById(R.id.delete8);
        edit[7] = view.findViewById(R.id.edit8);
        delete[8] = view.findViewById(R.id.delete9);
        edit[8] = view.findViewById(R.id.edit9);
        delete[9] = view.findViewById(R.id.delete10);
        edit[9] = view.findViewById(R.id.edit10);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ((Mabi3atNavigator) getActivity())
                .setActionBarTitle("عمليات المنتجات");

        MakeEditAction();

    }

    private void MakeEditAction() {
        for (int x=0; x<10; x++){
            edit[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Home.class);
                    intent.putExtra("fragment","edit_control");
                    startActivity(intent);
                }
            });
        }
    }
}
