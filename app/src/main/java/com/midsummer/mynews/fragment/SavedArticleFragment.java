package com.midsummer.mynews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.midsummer.mynews.R;
import com.midsummer.mynews.adapter.NewestArticleAdapter;
import com.midsummer.mynews.helper.IOnLoadFragmentObserver;
import com.midsummer.mynews.model.article.Result;
import com.midsummer.mynews.model.article.SavedModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nienb on 14/2/16.
 */
public class SavedArticleFragment extends Fragment implements IOnLoadFragmentObserver{

    @Bind(R.id.saved_article_recycler_view)
    UltimateRecyclerView mRecyclerView;

    NewestArticleAdapter mAdapter = null;
    LinearLayoutManager linearLayoutManager;

    public static SavedArticleFragment newInstance(){
        return new SavedArticleFragment();
    }
    public SavedArticleFragment() {
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
        View v = inflater.inflate(R.layout.fragment_saved_article_main, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setEmptyView(getResources().getIdentifier("fragment_saved_article_instruction", "layout",
                getContext().getPackageName()));

        processLoadSavedArticle();
        mRecyclerView.disableLoadmore();

        mRecyclerView.enableDefaultSwipeRefresh(true);
        mRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                processLoadSavedArticle();
            }
        });
        return v;
    }
    public void processLoadSavedArticle() {
        List<Result> mModel = SavedModel.getAll();
        if (mModel.size() == 0){
            mRecyclerView.setAdapter(null);
            mRecyclerView.showEmptyView();
        }else{
            mAdapter = new NewestArticleAdapter(mModel, getContext());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoadFragment(Object arg) {
        processLoadSavedArticle();
    }
}
