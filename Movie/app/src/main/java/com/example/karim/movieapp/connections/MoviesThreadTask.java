package com.example.karim.movieapp.connections;

//responsible for receiving data from server

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.karim.movieapp.model.Movie;
import com.example.karim.movieapp.views.MovieFragment;

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

public class MoviesThreadTask extends AsyncTask<String, Void, String> {


    ProgressDialog progressDialog;
    private MovieFragment movieFragment;


    public MoviesThreadTask(MovieFragment movieFragment) {

        this.movieFragment = movieFragment;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(movieFragment.getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {


        String link = strings[0];


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        try {

            URL url = new URL(link);

            // Create the request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            return readStream(inputStream);

        } catch (IOException e) {

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                     Log.e("MainActivity", "Error closing stream", e);

                }
            }
        }
    }


    @Override
    protected void onPostExecute(String data) {

        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray moviesArray = jsonObject.getJSONArray("results");
                //create Array list to save data
                List<Movie> movies = new ArrayList<>();
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObject = moviesArray.getJSONObject(i);
                    Movie movie = new Movie(
                            movieObject.getInt("id"),
                            movieObject.getString("original_title"),
                            movieObject.getString("poster_path"),
                            movieObject.getString("overview"),
                            movieObject.getString("vote_average"),
                            movieObject.getString("release_date"));
                    movies.add(movie);
                }
                progressDialog.dismiss();
                movieFragment.setAdapter(movies);

            } catch (JSONException e) {
                e.printStackTrace();
                // error in parsing data.
                progressDialog.dismiss();

            }


        } else {
            // error in get data , show error message to user.
            progressDialog.dismiss();
        }


    }


    //buffer reader method take single from input stream ,
// save data which flows into streams

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}