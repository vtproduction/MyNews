package com.midsummer.mynews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.midsummer.mynews.API.APIEndpoint;
import com.midsummer.mynews.API.APIService;
import com.midsummer.mynews.model.APIResponse;
import com.midsummer.mynews.model.Result;
import com.midsummer.mynews.model.SavedModel;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //TestModel t = new TestModel("zero","one");
        //t.save();


        APIEndpoint p = APIService.build();
        Call<APIResponse> call = p.getLastestArticle(1);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Response<APIResponse> response, Retrofit retrofit) {
                APIResponse APIResponse = response.body();
                for (Result res: APIResponse.response.results
                     ) {
                    Log.d("MYTAG", res.webTitle + " - " + res.webPublicationDate);
                    SavedModel.ViewModel2SavedModel(res).save();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("MYTAG",t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
