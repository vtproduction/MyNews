package com.midsummer.mynews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.midsummer.mynews.API.APIEndpoint;
import com.midsummer.mynews.API.APIService;
import com.midsummer.mynews.R;
import com.midsummer.mynews.adapter.TopicAdapter;
import com.midsummer.mynews.model.topic.Topic;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by nienb on 13/2/16.
 */
public class TopicFragment extends Fragment{
    private GridLayoutManager mManager;
    @Bind(R.id.topic_main_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.topic_main_textfield)
    EditText mSearchEdittext;
    TopicAdapter mAdapter = null;

    public static TopicFragment newInstance(){
        return new TopicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topic_main, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setHasFixedSize(false);
        mManager = new GridLayoutManager(getContext(),3);
        mRecyclerView.setLayoutManager(mManager);
        processLoadNewArticles(true);
        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }


    public void processLoadNewArticles(final boolean showloading){
        if (showloading){

        }
        APIEndpoint api = APIService.build();
        Call<Topic> call = api.getTopic();
        call.enqueue(new Callback<Topic>() {
            @Override
            public void onResponse(Response<Topic> response, Retrofit retrofit) {
                mAdapter = new TopicAdapter(getContext(),response.body().getResponse().getResults());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Throwable t) {
                if (showloading) {
                }

                Snackbar.make(getActivity().findViewById(R.id.main_coordinatorlayout),
                        getResources().getString(R.string.cannot_load_article), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.go_offline), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }
        });
    }
}
