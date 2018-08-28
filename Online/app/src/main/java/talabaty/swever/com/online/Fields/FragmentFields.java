package talabaty.swever.com.online.Fields;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import talabaty.swever.com.online.Fields.MostTrend.FragmentMostTrend;
import talabaty.swever.com.online.Fields.MostViewed.FragmentMostViewed;
import talabaty.swever.com.online.R;

public class FragmentFields extends Fragment {

    private TabLayout tabLayout;
    ViewPager viewPager = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        FragmentManager fragmentManager = getFragmentManager();
        viewPager.setAdapter(new pager(fragmentManager));
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class pager extends FragmentPagerAdapter {

        public pager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new FragmentMostTrend().setType("trend");
            } else if (position == 1) {
                fragment = new FragmentMostViewed().setType("trend");
            } else if (position == 2) {
                fragment = new FragmentMostTrend().setType("trend");
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
