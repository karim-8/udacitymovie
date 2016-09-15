package com.example.karim.movieapp.views;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.karim.movieapp.R;
import com.example.karim.movieapp.adapters.GridAdapter;
import com.example.karim.movieapp.connections.MoviesThreadTask;
import com.example.karim.movieapp.database.DatabaseAdapter;
import com.example.karim.movieapp.model.Movie;
import com.example.karim.movieapp.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment  extends Fragment{

    List<Movie> movies;
    GridView moviesGridView;
    String link;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.movie_grid_fragment, container,false);

        //grid fragment     fragment
        moviesGridView = (GridView) view.findViewById(R.id.grid_movies);


        if (savedInstanceState == null){
            link = Utilities.POPULAR_LINK;

            if (Utilities.isNetworkAvailable(getActivity())){

                TextView textView = (TextView) view.findViewById(R.id.errortextview);
                textView.setVisibility(View.INVISIBLE);
                startTask(link);

            }else{
                final TextView textView = (TextView) view.findViewById(R.id.errortextview);
                textView.setVisibility(View.VISIBLE);textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Utilities.isNetworkAvailable(getActivity())) {
                            startTask(link);
                            textView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }


        }else {

            movies = savedInstanceState.getParcelableArrayList("list");
             setAdapter(movies);
            setGridItemListener();

        }

        return view;
    }

    public void setAdapter(List<Movie> movies) {
        this.movies= movies;
        GridAdapter gridAdapter = new GridAdapter(getActivity(),this.movies);
        moviesGridView.setAdapter(gridAdapter);
        setGridItemListener();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) movies);
    }


    public void startTask(String link){
        MoviesThreadTask moviesThreadTask = new MoviesThreadTask(this);
        moviesThreadTask.execute(link);

    }

    public void showFavouritesMovies(){
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(getActivity());
        movies =  databaseAdapter.getAllMovies();
        setAdapter(movies);
    }

    public void setGridItemListener() {
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = movies.get(i);
               ((MainActivity)MovieFragment.this.getActivity()).sendMovie(movie);

            }
        });
    }
}
