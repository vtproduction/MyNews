package com.midsummer.mynews.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.midsummer.mynews.API.APIEndpoint;
import com.midsummer.mynews.API.APIService;
import com.midsummer.mynews.R;
import com.midsummer.mynews.helper.CustomFadingActionBarHelper;
import com.midsummer.mynews.model.article.APIResponse;
import com.midsummer.mynews.model.article.Result;
import com.midsummer.mynews.model.article.SavedModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
    @Bind(R.id.article_detail_morein)
    TextView mTextMorein;
    @Bind(R.id.article_detail_webview)
    WebView  mWebView;
    @Bind(R.id.article_main_layout)
    LinearLayout mMainLayout;
    @Bind(R.id.article_detail_loading)
    LinearLayout mLoadingLayout;
    @Bind(R.id.article_detail_related_layout)
    LinearLayout mRelatedLayout;
    @Bind(R.id.article_detail_seeall)
    TextView mTextSeeAll;
    private boolean flag_isNight = false;
    private boolean flag_isLarge = false;
    private boolean flag_isSaved = false;
    private static final PrettyTime PT = new PrettyTime();
    private Typeface font;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        mModel =  Parcels.unwrap(getIntent().getParcelableExtra("article"));
        if (mModel == null){
            this.finish();
        }
        this.flag_isSaved = SavedModel.isItemInDB(mModel.id);
        CustomFadingActionBarHelper helper = new CustomFadingActionBarHelper()
                .actionBarBackground(R.color.base_color_1)
                .headerLayout(R.layout.article_detail_large_image)
                .contentLayout(R.layout.article_detail_main);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        setupActionbar();
        ButterKnife.bind(this);
        displayContent();
        processLoadingRelatedArticle();
    }

    private void setupActionbar(){
        ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.article_detail_actionbar,
                null);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        Toolbar parent = (Toolbar) actionBarLayout.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parent.setElevation(0);
        }
    }

    private void displayContent(){
        Glide.with(this).load(SavedModel.extractImagelinkFromRawString(mModel.fields.main))
                .into((ImageView) findViewById(R.id.article_detail_image));
        this.font = Typeface.createFromAsset(this
                .getAssets(), String.format("font/%s", "titlefont.ttf"));
        mTextTitle.setText(mModel.webTitle);
        mTextTitle.setTypeface(font);
        mTextSection.setText(mModel.sectionName);
        mTextPostDate.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm").format(mModel.getPostDate()));
        mTextMorein.setText(getResources().getString(R.string.more_in) + " " + mModel.sectionName);
        if (flag_isSaved){
            mActionbar_savebtn.setImageResource(R.drawable.ic_action_bookmark_remove_fill);
        }else{
            mActionbar_savebtn.setImageResource(R.drawable.ic_action_bookmark_add_fill);
        }
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

    @OnClick(R.id.article_detail_seeall)
    public void onSeeAllTextPress(){
        Intent i = new Intent(this, TopicListActivity.class);
        i.putExtra("topicid", mModel.sectionId);
        i.putExtra("topicname", mModel.sectionName);
        startActivity(i);
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
            mActionbar_largerfontbtn.setImageResource(R.drawable.ic_action_font_bigger);
            settings.setTextSize(WebSettings.TextSize.LARGER);
        }else{
            flag_isLarge = true;
            settings.setTextSize(WebSettings.TextSize.LARGEST);
            mActionbar_largerfontbtn.setImageResource(R.drawable.ic_action_font_smaller);
        }
    }

    @OnClick(R.id.article_actionbar_lightbub)
    public void onLightBubBtnPress(){
        mActionbar_lightbubbtn.startAnimation(buttonClick);
        if (flag_isNight){
            flag_isNight = false;
            mActionbar_lightbubbtn.setImageResource(R.drawable.ic_action_lightoff);
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
            mActionbar_lightbubbtn.setImageResource(R.drawable.ic_action_lighton);
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



    @OnClick(R.id.article_actionbar_save)
    public void onSaveBtnPress(){
        SavedModel m = SavedModel.ViewModel2SavedModel(mModel);
        if (flag_isSaved){
            flag_isSaved  = false;
            //remove
            m.deleteFromDB();
            Toast.makeText(this,getResources().getString(R.string.article_removed),Toast.LENGTH_SHORT).show();
            mActionbar_savebtn.setImageResource(R.drawable.ic_action_bookmark_add_fill);
        }else {
            flag_isSaved = true;
            //insert
            m.saveToDB();
            Toast.makeText(this,getResources().getString(R.string.article_saved),Toast.LENGTH_SHORT).show();
            mActionbar_savebtn.setImageResource(R.drawable.ic_action_bookmark_remove_fill);
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

    public void processLoadingRelatedArticle(){
        APIEndpoint api = APIService.build();
        String[] dates = getRandomDateTime();
        Call<APIResponse> call = api.getArticlesBySection(mModel.sectionId,dates[0],dates[1]);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(final Response<APIResponse> response, Retrofit retrofit) {
                mLoadingLayout.setVisibility(View.GONE);
                if (response.body().response.results.size() > 0) {
                    mRelatedLayout.removeAllViews();
                    for (final Result result: response.body().response.results
                         ) {
                        View itemView = View.inflate(ArticleDetailActivity.this, R.layout.fragment_newestarticle_item, null);
                        TextView mTitle = (TextView) itemView.findViewById(R.id.item_title);
                        TextView mSubtitle = (TextView) itemView.findViewById(R.id.item_subtitle);
                        TextView mSection = (TextView) itemView.findViewById(R.id.item_section);
                        TextView mPostDate = (TextView) itemView.findViewById(R.id.item_postdate);
                        ImageView mThumbnail = (ImageView) itemView.findViewById(R.id.item_thumbnail);
                        mTitle.setText(result.webTitle);
                        mSubtitle.setText(Html.fromHtml(result.fields.trailText).toString());
                        mSection.setText(result.sectionName);
                        mPostDate.setText(PT.format(result.getPostDate()));
                        Glide.with(ArticleDetailActivity.this).load(SavedModel.extractImagelinkFromRawString(result.fields.main))
                                .placeholder(R.drawable.ic_broken_image)
                                .error(R.drawable.ic_broken_image)
                                .into(mThumbnail);
                        mRelatedLayout.addView(itemView);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(ArticleDetailActivity.this, ArticleDetailActivity.class);
                                i.putExtra("article", Parcels.wrap(result));
                                startActivity(i);
                            }
                        });
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Throwable t) {
                mLoadingLayout.setVisibility(View.GONE);
            }
        });
    }

    private String[] getRandomDateTime(){
        Calendar today = Calendar.getInstance();
        int year = randBetween(2015, today.get(Calendar.YEAR));
        int month;
        do {
            month = randBetween(1, 12);
        }while ((year + month) > (today.get(Calendar.YEAR) + today.get(Calendar.MONTH)));
        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int day = randBetween(1, 28);
        DateTime startDate = new DateTime(year, month, day, 0,0,0);
        DateTime endDate = startDate.plusMonths(6);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        return new String[]{dtf.print(startDate), dtf.print(endDate)};
    }

    public int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
