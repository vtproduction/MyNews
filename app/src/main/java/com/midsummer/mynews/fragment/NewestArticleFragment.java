package com.midsummer.mynews.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.midsummer.mynews.API.APIEndpoint;
import com.midsummer.mynews.API.APIService;
import com.midsummer.mynews.MainActivity;
import com.midsummer.mynews.R;
import com.midsummer.mynews.adapter.NewestArticleAdapter;
import com.midsummer.mynews.model.article.APIResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by nienb on 11/2/16.
 */
public class NewestArticleFragment extends Fragment{


    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView mRecyclerView;

    NewestArticleAdapter mAdapter = null;
    LinearLayoutManager linearLayoutManager;
    private boolean isFirstLoad = true;

    public static NewestArticleFragment newInstance(){
        return new NewestArticleFragment();
    }
    public NewestArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setEmptyView(getResources().getIdentifier("loading_view", "layout",
                getContext().getPackageName()));

        mRecyclerView.enableLoadmore();
        mRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                page++;
                processLoadNewArticles(false);
            }
        });

        mRecyclerView.enableDefaultSwipeRefresh(true);
        mRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                processLoadNewArticles(true);
            }
        });

        processLoadNewArticles(true);
        return v;
    }

    int page = 1;
    public void processLoadNewArticles(final boolean showloading){
        Log.d("MYTAG", "processLoadNewArticles: " + page);
        if (showloading){
            page = 1;
            mRecyclerView.getEmptyView().findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
            ((TextView)mRecyclerView.getEmptyView().findViewById(R.id.textView2))
                    .setText(getResources().getString(R.string.loading));
            mRecyclerView.getEmptyView().findViewById(R.id.textView2).setOnClickListener(null);
            mRecyclerView.showEmptyView();
        }
        APIEndpoint api = APIService.build();
        Call<APIResponse> call = api.getLastestArticle(page);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response, Retrofit retrofit) {
                Log.d("MYTAG", "processLoadNewArticles-onResponse: " + response.body().response.results.size());
                mRecyclerView.hideEmptyView();
                //reach the maximum page
                if (page >= response.body().response.pages) {
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.no_more_news), Toast.LENGTH_SHORT).show();
                }

                //fetch nothing
                if (response.body().response.results.size() == 0){
                    mRecyclerView.getEmptyView().findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
                    ((TextView)mRecyclerView.getEmptyView().findViewById(R.id.textView2))
                            .setText(getResources().getString(R.string.no_article_found));
                    mRecyclerView.showEmptyView();
                    return;
                }

                //first load
                if (page == 1) {
                    mAdapter = new NewestArticleAdapter(response.body().response.results, getContext());
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setCustomLoadMoreView(LayoutInflater.from(getContext())
                            .inflate(R.layout.loading_more_view, null));
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.insert(response.body().response.results, mAdapter.getAdapterItemCount());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (showloading){
                    mRecyclerView.getEmptyView().findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
                    ((TextView)mRecyclerView.getEmptyView().findViewById(R.id.textView2))
                            .setText(getResources().getString(R.string.con_err_tap_retry));
                    mRecyclerView.showEmptyView();
                    mRecyclerView.getEmptyView().findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            processLoadNewArticles(true);
                        }
                    });
                }
                Snackbar.make(getActivity().findViewById(R.id.main_coordinatorlayout),
                        getResources().getString(R.string.cannot_load_article), Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.go_offline), new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                ((MainActivity)getActivity()).setPage(3);
                            }
                        }).show();
            }
        });
    }


}
