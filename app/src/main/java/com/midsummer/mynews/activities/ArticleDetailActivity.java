package com.midsummer.mynews.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midsummer.mynews.R;
import com.midsummer.mynews.helper.CustomFadingActionBarHelper;
import com.midsummer.mynews.model.Result;
import com.midsummer.mynews.model.SavedModel;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nienb on 13/2/16.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    private Result mModel;

    @Bind(R.id.article_actionbar_backbtn)
    ImageButton mActionbar_backbtn;
    @Bind(R.id.article_actionbar_largerfont)
    ImageButton mActionbar_largerfontbtn;
    @Bind(R.id.article_actionbar_lightbub)
    ImageButton mActionbar_lightbubbtn;
    @Bind(R.id.article_actionbar_save)
    ImageButton  mActionbar_savebtn;
    @Bind(R.id.article_actionbar_share)
    ImageButton  mActionbar_sharebtn;
    @Bind(R.id.article_detail_title)
    TextView mTextTitle;
    @Bind(R.id.article_detail_section)
    TextView mTextSection;
    @Bind(R.id.article_detail_postdate)
    TextView mTextPostDate;
    @Bind(R.id.article_detail_webview)
    WebView  mWebView;
    @Bind(R.id.article_main_layout)
    LinearLayout mMainLayout;
    private boolean flag_isNight = false;
    private boolean flag_isLarge = false;
    private boolean flag_isSaved = false;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        mModel =  Parcels.unwrap(getIntent().getParcelableExtra("article"));
        if (mModel == null){
            this.finish();
        }

        CustomFadingActionBarHelper helper = new CustomFadingActionBarHelper()
                .actionBarBackground(R.color.base_color_1)
                .headerLayout(R.layout.article_detail_large_image)
                .contentLayout(R.layout.article_detail_main);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        setupActionbar();
        ButterKnife.bind(this);
        displayContent();
    }

    private void setupActionbar(){
        ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.article_detail_actionbar,
                null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    private void displayContent(){
        Glide.with(this).load(SavedModel.extractImagelinkFromRawString(mModel.fields.main))
                .into((ImageView) findViewById(R.id.article_detail_image));
        mTextTitle.setText(mModel.webTitle);
        mTextSection.setText(mModel.sectionName);
        mTextPostDate.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm").format(mModel.getPostDate()));

        WebSettings settings = mWebView.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setTextSize(WebSettings.TextSize.LARGER);
        mWebView.loadData("<div style='padding:0'>" + mModel.fields.body
                .replace("class=\"element element-video\"", "class=\"element element-video\" style='display:none'")
                .replace("<img", "<img style='width:100%; height:auto'") + "</div>", "text/html; charset=utf-8", "UTF-8");
        mWebView.setBackgroundColor(Color.TRANSPARENT);
    }

    @OnClick(R.id.article_actionbar_backbtn)
    public void onBackbtnPress(){
        mActionbar_backbtn.startAnimation(buttonClick);
        this.finish();
    }

    @OnClick(R.id.article_actionbar_largerfont)
    public void onLargerfontBtnPress(){
        mActionbar_largerfontbtn.startAnimation(buttonClick);
        WebSettings settings = mWebView.getSettings();
        if (flag_isLarge){
            flag_isLarge = false;
            settings.setTextSize(WebSettings.TextSize.LARGER);
        }else{
            flag_isLarge = true;
            settings.setTextSize(WebSettings.TextSize.LARGEST);
        }
    }

    @OnClick(R.id.article_actionbar_lightbub)
    public void onLightBubBtnPress(){
        mActionbar_lightbubbtn.startAnimation(buttonClick);
        if (flag_isNight){
            flag_isNight = false;
            mMainLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
            mTextTitle.setTextColor(getResources().getColor(R.color.base_color_1));
            mTextPostDate.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            mTextSection.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            mWebView.loadData("<div style='padding:0; color:black;'>" + mModel.fields.body
                    .replace("class=\"element element-video\"", "class=\"element element-video\" style='display:none'")
                    .replace("<img", "<img style='width:100%; height:auto'") + "</div>", "text/html; charset=utf-8", "UTF-8");
            mWebView.setBackgroundColor(Color.TRANSPARENT);
        }else{
            flag_isNight = true;
            mMainLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextTitle.setTextColor(getResources().getColor(R.color.base_color_4));
            mTextPostDate.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
            mTextSection.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
            mWebView.loadData("<div style='padding:0; color:white;'>" + mModel.fields.body
                    .replace("class=\"element element-video\"", "class=\"element element-video\" style='display:none'")
                    .replace("<img", "<img style='width:100%; height:auto'") + "</div>", "text/html; charset=utf-8", "UTF-8");
            mWebView.setBackgroundColor(Color.TRANSPARENT);
        }

    }


    @OnClick(R.id.article_actionbar_share)
    public void onShareBtnPress(){
        mActionbar_sharebtn.startAnimation(buttonClick);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mModel.webTitle);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mModel.webUrl);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
