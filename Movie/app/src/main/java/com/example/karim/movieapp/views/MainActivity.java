package com.example.karim.movieapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.karim.movieapp.R;
import com.example.karim.movieapp.model.Movie;
import com.example.karim.movieapp.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) movies);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MovieFragment movieFragment= (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.grid_fragment);

        switch (item.getItemId()) {

            case R.id.favourite:
                movieFragment.showFavouritesMovies();
            return  true;
            case R.id.top_rated:
                movieFragment.startTask(Utilities.Top_Rated);
                return true;

            case R.id.most_pop:
                movieFragment.startTask(Utilities.POPULAR_LINK);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public boolean isDevicePortrait()
    {
        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void sendMovie(Movie movie){
        if(isDevicePortrait()){
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("movie",movie);
           startActivity(intent);
        }else{
            MovieDetailFragment detailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.movie_detail_fragment);
            detailFragment.setData(movie);
        }
    }

}
