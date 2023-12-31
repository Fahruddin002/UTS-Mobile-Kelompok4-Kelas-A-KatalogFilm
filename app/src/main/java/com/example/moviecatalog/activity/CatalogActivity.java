package com.example.moviecatalog.activity;

import static com.example.moviecatalog.config.ServerConfig.API_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import com.example.moviecatalog.config.ServerConfig;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.moviecatalog.R;
import com.example.moviecatalog.adapter.MovieAdapter;
import com.example.moviecatalog.model.Response;
import com.example.moviecatalog.model.Result;
import com.example.moviecatalog.rest.APIClient;
import com.example.moviecatalog.rest.APIInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CatalogActivity extends AppCompatActivity {

    private MovieAdapter adapter;
    String CATEGORY = "popular";
    int PAGE = 1;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_movie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CallRetrofit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_about) {
            Intent intentAbout = new Intent(this, AboutActivity.class);
            startActivity(intentAbout);
        } else if (id == R.id.menu_profile) {
            Intent intentProfile = new Intent(this, ProfileActivity.class);
            startActivity(intentProfile);
        }
        return true;
    }

    private void CallRetrofit() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Response> responseCall = apiInterface.getMovie(CATEGORY, ServerConfig.API_KEY, PAGE);
        responseCall.enqueue(new Callback<Response>() {

            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Result> resultList = response.body().getResults();
                    if (resultList != null) {
                        adapter = new MovieAdapter(CatalogActivity.this, resultList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        // Handle the case where resultList is null
                    }
                } else {
                    // Handle the case where the response is not successful
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e("CatalogActivity", "Retrofit call failed", t);
            }
        });
    }
}