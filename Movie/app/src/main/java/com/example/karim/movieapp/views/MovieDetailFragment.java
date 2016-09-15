package com.example.karim.movieapp.views;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karim.movieapp.R;
import com.example.karim.movieapp.database.DatabaseAdapter;
import com.example.karim.movieapp.model.Movie;
import com.example.karim.movieapp.model.Trailer;
import com.example.karim.movieapp.utilities.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MovieDetailFragment extends Fragment implements View.OnClickListener {

    Movie movie;

    ImageButton bookmark;
    boolean isBookmarked;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView releaseDate;
    private TextView rateTextview;
    private TextView overviewTextview;
    TextView reviewsTextView;
    List<Trailer> trailers;
    List<String> reviews;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail_fragment, container, false);

        // find views.

        bookmark = (ImageButton) view.findViewById(R.id.favouriteButton);
        bookmark.setOnClickListener(this);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        releaseDate = (TextView) view.findViewById(R.id.tv_release_date);
        rateTextview = (TextView) view.findViewById(R.id.tv_rate);
        overviewTextview = (TextView) view.findViewById(R.id.tv_overview);
        reviewsTextView = (TextView) view.findViewById(R.id.tv_review);


        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable("movie");
            setData(movie);


        }
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("movie", movie);
    }


    protected void setData(Movie movie) {
        this.movie = movie;
        titleTextView.setText(movie.getTitle());
        releaseDate.setText(movie.releaseDate);
        rateTextview.setText(movie.rate);
        overviewTextview.setText(movie.overview);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movie.getImage()).into(imageView);

        DatabaseAdapter databaseAdapter = new DatabaseAdapter(getActivity());
        isBookmarked = databaseAdapter.isMovieBookmarked(movie.title);
        if (isBookmarked) {
            bookmark.setImageResource(R.drawable.redheart);
        } else {
            bookmark.setImageResource(R.drawable.grayheart);
        }

        // get trailers.
        new ThreadTask(1,this.movie.id).execute();
        // get reviews.
        new ThreadTask(2,this.movie.id).execute();

    }


    /**
     * show movie trailer in youtube or other app.
     *
     * @param key movie key in youtube .
     */
    private void showMovieTrailer(String key) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + key));
        try {
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View view) {


        if (isBookmarked) {
            // delete
            DatabaseAdapter databaseAdapter = new DatabaseAdapter(getActivity());
            boolean isDeleted = databaseAdapter.deleteMovie(movie.title);
            if (isDeleted) {
                isBookmarked = false;
                bookmark.setImageResource(R.drawable.grayheart);
            } else {
                isBookmarked = true;
            }
        } else {
            // add
            boolean isAdd = new DatabaseAdapter(getActivity()).addMovie(movie);
            if (isAdd) {
                isBookmarked = true;
                bookmark.setImageResource(R.drawable.redheart);
            } else {
                isBookmarked = false;
                bookmark.setImageResource(R.drawable.grayheart);
            }

        }

    }


    /**
     * get list of movie reviews
     *
     * @param movieID movie id number.
     * @return list of movie reviews.
     */
    private List<String> getMovieReviews(int movieID) throws IOException, JSONException {

        List<String> reviews = null;
        // initialize connection on server.
        URL url = new URL(String.format(Utilities.REVIEWS_LINK, movieID));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();
        if (responseCode == 200) {
            // get data from response body.
            reviews = new ArrayList<>();
            String response = getStringResponse(urlConnection.getInputStream());
            urlConnection.disconnect();
            // parse data from response String body.
            JSONObject jsonObject = new JSONObject(response);
            JSONArray reviewsArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject reviewObject = reviewsArray.getJSONObject(i);
                reviews.add(reviewObject.getString("author") + " :- " + reviewObject.getString("content"));
            }

        } else {
            urlConnection.disconnect();
        }


        return reviews;
    }


    /**
     * get movie trailers by movie id.
     *
     * @param movieID movie id number.
     * @return list of movie trailers names.
     */
    private List<Trailer> getMovieTrailers(int movieID) throws IOException, JSONException {
        List<Trailer> trailers = null;
        // initialize connection on server.
        URL url = new URL(String.format(Utilities.TRAILER_LINK, movieID));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();
        if (responseCode == 200) {
            // connected to link successfully.
            trailers = new ArrayList<>();
            String response = getStringResponse(urlConnection.getInputStream());
            urlConnection.disconnect();
            // parse response data.
            JSONObject jsonObject = new JSONObject(response);
            JSONArray trailersArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < trailersArray.length(); i++) {
                JSONObject trailerObject = trailersArray.getJSONObject(i);
                if (trailerObject.getString("type").equalsIgnoreCase("Trailer")) {
                    // add trailer info.
                    trailers.add(new Trailer(trailerObject.getString("name"), trailerObject.getString("key")));

                }

            }
        } else {
            urlConnection.disconnect();
        }
        return trailers;

    }


    /**
     * get string response from input stream url.
     *
     * @param inputStream input stream
     * @return string response from stream.
     */
    String getStringResponse(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String data;
        while ((data = bufferedReader.readLine()) != null) {
            stringBuilder.append(data);
        }

        bufferedReader.close();
        return stringBuilder.toString();
    }

    class ThreadTask extends AsyncTask<String, Void, String> {

        private int type;
        private int id;

        public ThreadTask(int type, int id) {

            this.type = type;
            this.id = id;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (type == 1) {
                // get trailers.
                try {
                    trailers = getMovieTrailers(id);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // get reviews.
                try {
                    reviews = getMovieReviews(id);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if (type == 1 && trailers != null) {
                // show trailers.
                // set trailers listview adapter.

                ListView trailersListView = (ListView) getView().findViewById(R.id.list_trailers);
                trailersListView.setAdapter(new ArrayAdapter<>(getContext(),
                        R.layout.listview_movie_trailers, R.id.tv_movie_trailer_title, trailers));
                trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        showMovieTrailer(trailers.get(position).key);
                    }
                });
            } else {
                // show reviews.
                if (reviews != null) {
                    StringBuilder reviewsBuilder = new StringBuilder();
                    for (String review : reviews) {
                        reviewsBuilder.append(review);
                        reviewsBuilder.append("\n");
                    }
                    reviewsTextView.setText(reviewsBuilder.toString());
                }
            }
        }
    }

}
