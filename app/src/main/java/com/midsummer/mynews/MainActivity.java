package com.midsummer.mynews;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Window;
import android.view.WindowManager;

import com.midsummer.mynews.fragment.NewestArticleFragment;
import com.midsummer.mynews.fragment.SavedArticleFragment;
import com.midsummer.mynews.fragment.SearchFragment;
import com.midsummer.mynews.fragment.TopicFragment;
import com.midsummer.mynews.helper.TypefaceSpan;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.base_color_1);

        setSupportActionBar(toolbar);

        setupViewPager(mPager);
        mTabs.setupWithViewPager(mPager);
        setupTabIcons();

        SpannableString s = new SpannableString(getResources().getString(toolbarTitle[0]));
        s.setSpan(new TypefaceSpan(this, "titlefont.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }



    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void setupTabIcons(){
        for (int i = 0; i < mTabs.getTabCount(); i++){
            mTabs.getTabAt(i).setIcon(tabIcons[i]);

        }
    }

    public void setPage(int position){
        mPager.setCurrentItem(position, true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(NewestArticleFragment.newInstance(), "ONE");
        adapter.addFragment(TopicFragment.newInstance(), "TWO");
        adapter.addFragment(SearchFragment.newInstance(), "THREE");
        adapter.addFragment(SavedArticleFragment.newInstance(), "FOUR");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SpannableString s = new SpannableString(getResources().getString(toolbarTitle[position]));
                s.setSpan(new TypefaceSpan(MainActivity.this, "titlefont.ttf"), 0, s.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                getSupportActionBar().setTitle(s);
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
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
