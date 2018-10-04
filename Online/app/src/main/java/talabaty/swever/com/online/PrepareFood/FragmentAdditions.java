package talabaty.swever.com.online.PrepareFood;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import talabaty.swever.com.online.R;

public class FragmentAdditions extends Fragment {

    View view;
    Button next, previous;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<PrepareFood> sanfList;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_additions, container, false);
        next = view.findViewById(R.id.next);
        previous = view.findViewById(R.id.previous);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.rec_food);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new FadeInDownAnimator(new OvershootInterpolator(1f)));
        fragmentManager = getFragmentManager();
        sanfList = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        for (int x=0; x<10; x++){
            PrepareFood food = new PrepareFood(x,"ame","",15);
            sanfList.add(food);
        }

        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
        alphaAdapter.setDuration(3000);
        recyclerView.setAdapter(new AdditionsAdapter(getActivity(), sanfList, new AdditionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PrepareFood item) {
                // Add To Intent Or Chart Or Class
            }
        }));


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_home,new FragmentPrepareFood()).commit();
            }
        });
        //Todo: add to Chart
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_home,new FinishChart()).addToBackStack("FinishChart").commit();
//            }
//        });
    }
}
