package com.example.karim.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.karim.movieapp.model.Movie;
import com.example.karim.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class GridAdapter extends BaseAdapter {

    Context context;

    //refrence to the Movie class
    private List<Movie> movies;

    public GridAdapter(Context context, List<Movie> movies) {

        this.movies = movies;
        this.context=context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView  (int i, View view, ViewGroup viewGroup) {

        ImageView movieImageView;

        if (view == null) {


            view = LayoutInflater.from(context).inflate(R.layout.item_design, viewGroup, false);

            movieImageView = (ImageView) view.findViewById(R.id.grid_item_image);

            view.setTag(movieImageView);

        } else {
            movieImageView = (ImageView) view.getTag();

        }

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"
                +movies.get(i).getImage()).placeholder(R.drawable.download).into(movieImageView);

        return view;
    }
}
