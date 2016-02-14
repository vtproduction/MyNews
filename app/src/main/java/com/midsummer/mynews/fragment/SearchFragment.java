package com.midsummer.mynews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by nienb on 14/2/16.
 */
public class SearchFragment extends Fragment {
    @Bind(R.id.search_main_recyclerview)
    UltimateRecyclerView mRecyclerView;
    NewestArticleAdapter mAdapter = null;
    LinearLayoutManager linearLayoutManager;
    @Bind(R.id.search_main_textfield)
    EditText mSearchEdittext;
    @Bind(R.id.sgt_1)
    TextView mTextSuggest1;
    @Bind(R.id.sgt_2)
    TextView mTextSuggest2;
    @Bind(R.id.sgt_3)
    TextView mTextSuggest3;
    @Bind(R.id.search_suggestlayout)
    LinearLayout mSearchSuggestLayout;
    int page = 1,  totalPage = -1;
    String query;
    public static SearchFragment newInstance(){
        return new SearchFragment();
    }
    public SearchFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_main, container, false);
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
                processSearchArticle(false);
            }
        });

        mSearchEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (mSearchEdittext.getText().length() > 0) {
                        query = mSearchEdittext.getText().toString();
                        processSearchArticle(true);
                    }
                    return true;
                }
                return false;
            }
        });

        mSearchEdittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (mSearchEdittext.getRight() - mSearchEdittext.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        query = mSearchEdittext.getText().toString();
                        processSearchArticle(true);
                        return true;
                    }else{
                        mSearchEdittext.requestFocus();
                    }
                }
                return false;
            }
        });

        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    mSearchSuggestLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    @OnClick(R.id.sgt_1)
    public void onSgt1Click(){
        query = mTextSuggest1.getText().toString();
        mSearchEdittext.setText(query);
        processSearchArticle(true);
    }
    @OnClick(R.id.sgt_2)
    public void onSgt2Click(){
        query = mTextSuggest2.getText().toString();
        mSearchEdittext.setText(query);
        processSearchArticle(true);
    }
    @OnClick(R.id.sgt_3)
    public void onSgt3Click(){
        query = mTextSuggest3.getText().toString();
        mSearchEdittext.setText(query);
        processSearchArticle(true);
    }

    public void processSearchArticle(final boolean showloading){
        Log.d("MYTAG", "SEARCH: page: " + page + " - total page: " +  totalPage);
        //Clear edittext focus
        mSearchEdittext.clearFocus();
        mRecyclerView.requestFocus();

        //hide input keyboard
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //perform new search, clear recycleview and reset page & totalPage
        if (showloading){
            mRecyclerView.setAdapter(null);
            totalPage = -1;
            page = 1;
            mRecyclerView.getEmptyView().findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
            ((TextView)mRecyclerView.getEmptyView().findViewById(R.id.textView2))
                    .setText(getResources().getString(R.string.loading));
            mRecyclerView.getEmptyView().findViewById(R.id.textView2).setOnClickListener(null);
            mRecyclerView.showEmptyView();

        }

        //if current page reach total page, disable load more
        if (totalPage != -1 && page > totalPage){
            Toast.makeText(getContext(),
                    getResources().getString(R.string.no_more_news), Toast.LENGTH_SHORT).show();
            mRecyclerView.disableLoadmore();
            mRecyclerView.setRefreshing(false);
            return;
        }

        mRecyclerView.enableLoadmore();
        mRecyclerView.setVisibility(View.VISIBLE);
        mSearchSuggestLayout.setVisibility(View.INVISIBLE);
        mRecyclerView.bringToFront();
        query = query.replace(" ","%20");

        APIEndpoint api = APIService.build();
        Call<APIResponse> call = api.getArticleBySearch(query, page);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response, Retrofit retrofit) {
                mRecyclerView.hideEmptyView();
                try{
                    totalPage = response.body().response.pages;
                    Log.d("MYTAG", "SEARCH: onResponse: page: " + page + " - total page: " +  totalPage + " - size: " + response.body().response.results.size());
                    //fetch nothing
                    if (response.body().response.results.size() == 0){
                        mRecyclerView.getEmptyView().findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
                        ((TextView)mRecyclerView.getEmptyView().findViewById(R.id.textView2))
                                .setText(getResources().getString(R.string.no_article_found));
                        mRecyclerView.showEmptyView();
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
                }catch (NullPointerException e){
                    Log.d("MYTAG","Exception: " + e.getMessage());
                    mRecyclerView.disableLoadmore();
                }catch (Exception e){
                    Log.d("MYTAG","Exception: " + e.getMessage());
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
                            processSearchArticle(true);
                        }
                    });
                    mRecyclerView.showEmptyView();
                }
                mRecyclerView.setRefreshing(false);
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
