package com.example.shen.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2015/11/23.
 */
public class MainActivity extends AppCompatActivity implements NavActionBarTab.onTabSelectedListener {

    NavActionBarTab actionBarTab;
    ViewPager viewpager;

    List<Fragment> sources = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        actionBarTab = (NavActionBarTab) findViewById(R.id.action_tab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        actionBarTab.setListener(this);
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("color", Color.BLACK);
        fragment.setArguments(bundle);

        sources.add(fragment);

        MyFragment fragment1 = new MyFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("color", Color.RED);
        fragment1.setArguments(bundle1);

        sources.add(fragment1);

        MyFragment fragment2 = new MyFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("color", Color.BLUE);
        fragment2.setArguments(bundle2);

        sources.add(fragment2);

        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewpager.addOnPageChangeListener(listener);

        actionBarTab.setBadge(1,true);

    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset > 0f) {
                actionBarTab.setPositionOffsetPixels(position, positionOffset);
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == 0) {
                actionBarTab.setSelection(viewpager.getCurrentItem());
            }
        }
    };

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return sources.get(position);
        }

        @Override
        public int getCount() {
            return sources.size();
        }
    }

    class MyFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setBackgroundColor(getArguments().getInt("color", 0));
            return imageView;
        }
    }

    @Override
    public void selected(int position) {
        viewpager.setCurrentItem(position - 1);
    }
}
