package com.example.karim.movieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.karim.movieapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter  {

    SQLiteHelper helper;

    public DatabaseAdapter (Context context) {

        helper = new SQLiteHelper(context);
    }

    public boolean addMovie(Movie movie)
    {
        boolean isBookmarked ;
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id",movie.id);
        contentValues.put(SQLiteHelper.TITLE,movie.title);
        contentValues.put(SQLiteHelper.IMAGE,movie.image);
        contentValues.put(SQLiteHelper.OVERVIEW,movie.overview);
        contentValues.put(SQLiteHelper.RELEASEDATE,movie.rate);
        contentValues.put(SQLiteHelper.RATE,movie.releaseDate);


        isBookmarked =  db.insert(SQLiteHelper.TABLE_NAME , null , contentValues)>0;
        return isBookmarked;

    }

    public List<Movie> getAllMovies ()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor =  db.rawQuery("select * from `moviestable`",null);
        List<Movie> movies = new ArrayList<>(cursor.getCount());

        if(cursor.moveToFirst()){
            do {
                movies.add(new Movie(
                        cursor.getInt(0),
                        cursor.getString(1).replaceAll("'",""),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movies;
    }


    public boolean isMovieBookmarked(String title){
        boolean isBookmarked  = false;
        SQLiteDatabase sqLiteDatabase  = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from `moviestable` where `_title`='"+title.replace("'","")+"'",null);
        if(cursor.moveToFirst()){
            isBookmarked = true;
        }
        cursor.close();
        sqLiteDatabase.close();
        return isBookmarked;
    }


    public boolean deleteMovie(String title){
        boolean isDeleted;
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        isDeleted = sqLiteDatabase.delete("moviestable"," `_title` = '"+title+"'",null)>0;
        sqLiteDatabase.close();
        return isDeleted;
    }







public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "moviesdatabase";
    private static final String TABLE_NAME = "moviestable";
    private static final String FAVOURITE= "favourite";
    private static final String TITLE="_title";
    private static final String IMAGE="image";
    private static final String OVERVIEW="overview";
    private static final String RATE="rate";
    private static final String RELEASEDATE="releasedate";
    private Context context;




    private static final String DROP_TABLE= "DROP TABLE"+TABLE_NAME+"IF EXISTS";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
      final String CREATE_TABLE = "create table `moviestable` (`id` integer ,`_title` text ,`image` text ,`overview` text," +
              "`rate` text ,`releasedate` text ) ";
            db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

            db.execSQL(DROP_TABLE);
            onCreate(db);

    }
}}
