package com.zaf.bakingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.zaf.bakingapp.adapters.CakesAdapter;
import com.zaf.bakingapp.models.Cake;
import com.zaf.bakingapp.network.GetDataService;
import com.zaf.bakingapp.network.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CakesAdapter.CakesAdapterListItemClickListener{

    private static final String TAG = "Main Activity";
    ProgressDialog progressDialog;
    private CakesAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading ingredients....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Cake>> call = service.getAllCakes();
        call.enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(Call<List<Cake>> call, Response<List<Cake>> response) {
                progressDialog.dismiss();
                generateCakeList(response.body());
            }

            @Override
            public void onFailure(Call<List<Cake>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Retrofit callback onFailure: " + t);
            }
        });
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateCakeList(List<Cake> cakeList) {

        recyclerView = findViewById(R.id.cakeListRecyclerView);
        adapter = new CakesAdapter(this, cakeList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(int item) {
        //TODO: Intent to new Activity

    }
}
