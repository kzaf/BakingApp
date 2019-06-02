package com.zaf.bakingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.zaf.bakingapp.adapters.CakesAdapter;
import com.zaf.bakingapp.models.Cake;
import com.zaf.bakingapp.network.GetDataService;
import com.zaf.bakingapp.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CakesAdapter.CakesAdapterListItemClickListener{

    private static final String TAG = "Main Activity";
    ProgressDialog progressDialog;
    TextView mErrorDisplay;
    private ArrayList<Cake> cakeList;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDialog();
        fetchCakes();

        getIdlingResource();
    }

    private void initializeDialog() {
        mErrorDisplay = findViewById(R.id.tv_error_message_display);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading ingredients....");
        progressDialog.show();
    }

    private void fetchCakes() {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);// for espresso testing
        }
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Cake>> call = service.getAllCakes();
        call.enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(Call<List<Cake>> call, Response<List<Cake>> response) {
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);// for espresso testing
                }
                progressDialog.dismiss();
                generateCakeList(response.body());
                if (response.body() != null) {
                    cakeList = new ArrayList<>(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Cake>> call, Throwable t) {
                progressDialog.dismiss();
                mErrorDisplay.setText("Something went wrong...Please try again!");
                Toast.makeText(MainActivity.this, "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Retrofit callback onFailure: " + t);
            }
        });
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateCakeList(List<Cake> cakeList) {

        RecyclerView recyclerView = findViewById(R.id.cakeListRecyclerView);
        CakesAdapter adapter = new CakesAdapter(this, cakeList);
        RecyclerView.LayoutManager layoutManager;

        layoutManager = new GridLayoutManager(MainActivity.this, calculateNoOfColumns(this));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    public void onListItemClick(int item) {

        Intent intent = new Intent(this, DetailsScreenActivity.class);
        intent.putExtra("Cake", cakeList.get(item));

        startActivity(intent);
    }
}
