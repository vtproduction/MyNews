package com.midsummer.mynews.activities;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.midsummer.mynews.API.APIEndpoint;
import com.midsummer.mynews.API.APIService;
import com.midsummer.mynews.MainActivity;
import com.midsummer.mynews.R;
import com.midsummer.mynews.adapter.NewestArticleAdapter;
import com.midsummer.mynews.model.article.APIResponse;
import com.midsummer.mynews.model.topic.Result;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by nienb on 14/2/16.
 */
public class TopicListActivity extends AppCompatActivity {

    private Result mTopic;
    private String topicname, topicid; //starting activity with non-model sent

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView mRecyclerView;
    @Bind(R.id.article_actionbar_backbtn)
    ImageButton mBackBtn;
    @Bind(R.id.actionbar_title)
    TextView mActionbarTitle;
    NewestArticleAdapter mAdapter = null;
    LinearLayoutManager linearLayoutManager;
    int page = 1;
    private Typeface font;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topiclist_main);
        setupActionbar();
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.base_color_1);


        mTopic =  Parcels.unwrap(getIntent().getParcelableExtra("topic"));
        if (mTopic == null){
            topicid = getIntent().getStringExtra("topicid");
            topicname = getIntent().getStringExtra("topicname");
        }else{
            topicid = mTopic.getId();
            topicname = mTopic.getWebTitle();
        }

        this.font = Typeface.createFromAsset(this
                .getAssets(), String.format("font/%s", "titlefont.ttf"));
        mActionbarTitle.setText(topicname);
        mActionbarTitle.setTypeface(font);
        mRecyclerView.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setEmptyView(getResources().getIdentifier("loading_view", "layout",getPackageName()));

        processLoadArticle(true);
        mRecyclerView.enableLoadmore();
        mRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                page++;
                processLoadArticle(false);
            }
        });

        mRecyclerView.enableDefaultSwipeRefresh(true);
        mRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                processLoadArticle(true);
            }
        });
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

    @OnClick(R.id.article_actionbar_backbtn)
    public void onBackBtnPressed(){
        this.finish();
    }

    private void setupActionbar(){
        ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.topic_list_activity_actionbar,
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

    private void processLoadArticle(final boolean showloading){
        if (showloading){
            mRecyclerView.getEmptyView().findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
            ((TextView)mRecyclerView.getEmptyView().findViewById(R.id.textView2))
                    .setText(getResources().getString(R.string.loading));
            mRecyclerView.showEmptyView();
        }
        APIEndpoint api = APIService.build();
        Call<APIResponse> call = api.getArticlesBySection(topicid, page);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response, Retrofit retrofit) {
                mRecyclerView.hideEmptyView();
                //reach the maximum page
                if (page >= response.body().response.pages) {
                    Toast.makeText(TopicListActivity.this,
                            getResources().getString(R.string.no_more_news), Toast.LENGTH_SHORT).show();
                }

                //fetch nothing
                if (response.body().response.results.size() == 0){
                    mRecyclerView.getEmptyView().findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
                    ((TextView)mRecyclerView.getEmptyView().findViewById(R.id.textView2))
                            .setText(getResources().getString(R.string.no_article_found));
                    mRecyclerView.showEmptyView();
                }

                //first load
                if (page == 1) {
                    mAdapter = new NewestArticleAdapter(response.body().response.results, TopicListActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setCustomLoadMoreView(LayoutInflater.from(TopicListActivity.this)
                            .inflate(R.layout.loading_more_view, null));

                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.insert(response.body().response.results, mAdapter.getAdapterItemCount());

                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (showloading){
                    mRecyclerView.showEmptyView();
                }
                mRecyclerView.setRefreshing(false);

                Snackbar.make(findViewById(R.id.main_coordinatorlayout),
                        getResources().getString(R.string.cannot_load_article), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.go_offline), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TopicListActivity.this.finish();
                                ((MainActivity)getApplicationContext()).setPage(3);
                            }
                        }).show();
            }
        });
    }
}
