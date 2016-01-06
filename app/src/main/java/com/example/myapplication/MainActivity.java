package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shen.actionbartab.NavActionBarTab;
import com.shen.test.R;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavActionBarTab actionBarTab = (NavActionBarTab) findViewById(R.id.action_bar_tab);

        actionBarTab.setBadge(2,true);

        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        actionBarTab.addViewPager(viewpager);

        final List<MyFragment> list = new ArrayList<>();
        list.add(MyFragment.createFragment(0));
        list.add(MyFragment.createFragment(1));
        list.add(MyFragment.createFragment(2));

        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
    }

    public static class MyFragment extends Fragment {

        int[] color = new int[]{Color.RED,Color.BLACK,Color.GREEN};

        public static MyFragment createFragment(int position){
            MyFragment fragment = new MyFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position",position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = new View(getActivity());
            view.setBackgroundColor(color[getArguments().getInt("position")]);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

    }
}
