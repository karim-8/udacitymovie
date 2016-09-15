package com.example.karim.movieapp.views;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.karim.movieapp.R;
import com.example.karim.movieapp.model.Movie;


public class MovieDetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // show action bar back arrow.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Movie movie= getIntent().getParcelableExtra("movie");

        MovieDetailFragment detailFragment = (MovieDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
        detailFragment.setData(movie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
