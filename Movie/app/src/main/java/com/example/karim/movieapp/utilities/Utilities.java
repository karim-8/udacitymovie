package com.example.karim.movieapp.utilities;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utilities {


    //put Your Keys here

    final static String API_KEY = "";
    public static String POPULAR_LINK = "http://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;
    public static String Top_Rated = "http://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;
    public static String TRAILER_LINK = "http://api.themoviedb.org/3/movie/%d/videos?api_key=" + API_KEY;
    public  static String REVIEWS_LINK = "http://api.themoviedb.org/3/movie/%d/reviews?api_key=" + API_KEY;

    //checking connections
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




}
