package com.midsummer.mynews;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.midsummer.mynews.fragment.NewestArticleFragment;
import com.midsummer.mynews.fragment.OneFragment;
import com.midsummer.mynews.fragment.SearchFragment;
import com.midsummer.mynews.fragment.TopicFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_container)
    ViewPager mPager;
    @Bind(R.id.tabs)
    TabLayout mTabs;

    private static final int[] tabIcons = {
            R.drawable.ic_action_news,
            R.drawable.ic_action_tiles_large,
            R.drawable.ic_search,
            R.drawable.ic_bookmark
    };

    private static final int[] toolbarTitle = {
            R.string.tab_newest,
            R.string.tab_topic,
            R.string.tab_search,
            R.string.tab_saved
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setupViewPager(mPager);
        mTabs.setupWithViewPager(mPager);
        setupTabIcons();

    }

    public void setupTabIcons(){
        for (int i = 0; i < mTabs.getTabCount(); i++){
            mTabs.getTabAt(i).setIcon(tabIcons[i]);

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(NewestArticleFragment.newInstance(), "ONE");
        adapter.addFragment(TopicFragment.newInstance(), "TWO");
        adapter.addFragment(SearchFragment.newInstance(), "THREE");
        adapter.addFragment(new OneFragment(), "FOUR");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(getResources().getString(toolbarTitle[position]));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }


    }
}
