package com.talabaty.swever.admin.AgentReports;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.talabaty.swever.admin.Mabi3at.Mabi3atNavigator;
import com.talabaty.swever.admin.R;

public class Fragment_agent_report extends Fragment {

    Button submit;
    ImageButton close;
    Button[] start;
    View message;
    View view;

    Button message_send;
    EditText message_title, message_content;
    Spinner message_type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_agent_report,container,false);
        start = new Button[10];

        start[0] = view.findViewById(R.id.start1);
        start[1] = view.findViewById(R.id.start2);
        start[2] = view.findViewById(R.id.start3);
        start[3] = view.findViewById(R.id.start4);
        start[4] = view.findViewById(R.id.start5);
        start[5] = view.findViewById(R.id.start6);
        start[6] = view.findViewById(R.id.start7);
        start[7] = view.findViewById(R.id.start8);
        start[8] = view.findViewById(R.id.start9);
        start[9] = view.findViewById(R.id.start10);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((Mabi3atNavigator) getActivity())
                .setActionBarTitle("تقارير العملاء");

        try {
            startMessageActions();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void startMessageActions() {

        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        message = inflater.inflate(R.layout.dialog_message_talabat, null);

        for (int x = 0; x < 10; x++) {
            start[x].setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    final int sdk = Build.VERSION.SDK_INT;
                    message_type = message.findViewById(R.id.messagetype);
                    message_title = message.findViewById(R.id.messagetitle);
                    message_content = message.findViewById(R.id.messagecontent);
                    message_send = message.findViewById(R.id.send);
                    close = message.findViewById(R.id.close);

                    message_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (message_type.getSelectedItem().toString().equals("مكالمه تليفونيه")) {

                                message_title.setVisibility(View.GONE);
                                message_content.setVisibility(View.GONE);
                                message_send.setText("اتصال");
                                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                    message_send.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.call_shape));
                                } else {
                                    message_send.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.call_shape));
                                }
                            } else if (message_type.getSelectedItem().toString().equals("رساله نصيه")) {

                                message_title.setVisibility(View.GONE);
                                message_content.setVisibility(View.VISIBLE);
                                message_send.setText("إرسال");
                                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                    message_send.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.button_shape));
                                } else {
                                    message_send.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_shape));
                                }
                            } else if (message_type.getSelectedItem().toString().equals("رساله عبر الإيميل")) {
                                message_title.setVisibility(View.VISIBLE);
                                message_content.setVisibility(View.VISIBLE);
                                message_send.setText("إرسال");
                                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                    message_send.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.button_shape));
                                } else {
                                    message_send.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_shape));
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("مراسله")
                            .setCancelable(false)
                            .setView(message)
                            .setNegativeButton("اغلاق", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do Nothing
                                    clearMessageView();
                                    dialog.dismiss();
                                }
                            });
                    final AlertDialog dialog2 = builder.create();
                    dialog2.show();
                    dialog2.getWindow().setLayout(1200, 800);

                    closeMessage(dialog2);
                    message_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitMessage(dialog2, message_type.getSelectedItem().toString());
                        }
                    });
//
                }

            });
        }

    }

    private void submitMessage(AlertDialog dialog, String s) {
        clearMessageView();
        if (s.equals("مكالمه تليفونيه")) {

            dialog.dismiss();
        } else if (s.equals("رساله نصيه")) {

            dialog.dismiss();
        } else if (s.equals("رساله عبر الإيميل")) {

            dialog.dismiss();
        }
    }

    private void closeMessage(final Dialog dialog) {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMessageView();
                dialog.dismiss();
            }
        });
    }

    private void clearMessageView() {
        if (message != null) {
            ViewGroup parent = (ViewGroup) message.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
    }
}
