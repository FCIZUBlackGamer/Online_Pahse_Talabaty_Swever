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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.ProductDetails.FragmentProductDetails;
import talabaty.swever.com.online.R;

public class FragmentPrepareFood extends Fragment {

    View view;
//    Button next;
    GridView gridView;
    PrepareFoodAdapter prepareFoodAdapter;
    List<PrepareFood> sanfList;
    FragmentManager fragmentManager;
    static int shopId;

    public static FragmentPrepareFood setData(int shopI){
        FragmentPrepareFood fragmentPrepareFood = new FragmentPrepareFood();
        shopId = shopI;
        return fragmentPrepareFood;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preapare_new_food, container, false);
//        next = view.findViewById(R.id.next);
        gridView = (GridView) view.findViewById(R.id.gridview);
        fragmentManager = getFragmentManager();
        sanfList = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        for (int x=0; x<10; x++){
            PrepareFood food = new PrepareFood(x,"ame","dd",12);
            sanfList.add(food);
        }

        prepareFoodAdapter = new PrepareFoodAdapter(getActivity(), sanfList);
        gridView.setAdapter(prepareFoodAdapter);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_center);
        gridView.setAnimation(anim);
        anim.start();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrepareFood book = sanfList.get(position);
                fragmentManager.beginTransaction().replace(R.id.frame_home, new FragmentAdditions()).addToBackStack("FragmentAdditions").commit();
            }
        });


//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_home,new FinishChart()).addToBackStack("FinishChart").commit();
//            }
//        });
    }
}
