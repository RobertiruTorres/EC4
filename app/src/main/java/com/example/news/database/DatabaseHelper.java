package com.example.news.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.news.models.ArticleModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "noticias";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "favoritos";

    //CAMPOS
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_URL_TO_IMAGE = "urlToImage";
    private static final String COLUMN_PUBLISHED_AT = "publishedAt";
    private static final String COLUMN_CONTENT = "content";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_TITLE + " TEXT UNIQUE, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_URL_TO_IMAGE + " TEXT, " +
                COLUMN_PUBLISHED_AT + " TEXT, " +
                COLUMN_CONTENT + " TEXT)";
        db.execSQL(createTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    // Método para obtener todos los favoritos
    public List<ArticleModel> getAllFavorites() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ArticleModel> favorites = new ArrayList<>();

        try {
            cursor = db.query(TABLE_NAME, null, null, null,
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL));
                    String urlToImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL_TO_IMAGE));
                    String publishedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLISHED_AT));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));

                    ArticleModel favorite = new ArticleModel(author, title, description, url, urlToImage, publishedAt, content);
                    favorites.add(favorite);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return favorites;
    }

    public ArticleModel getFavoriteByTitle(String articleTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        ArticleModel favorite = null;

        try {
            String selection = COLUMN_TITLE + " = ?";
            String[] selectionArgs = {articleTitle};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs,
                    null, null, null);


            if (cursor != null && cursor.moveToFirst()) {

                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL));
                String urlToImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL_TO_IMAGE));
                String publishedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLISHED_AT));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));

                favorite = new ArticleModel(author, title, description, url, urlToImage, publishedAt, content);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return favorite;
    }



    public long insertFavorite(String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_URL, url);
        values.put(COLUMN_URL_TO_IMAGE, urlToImage);
        values.put(COLUMN_PUBLISHED_AT, publishedAt);
        values.put(COLUMN_CONTENT, content);
        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }
    public int deleteFavoriteByTitle(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = 0;

        try {
            rowsAffected = db.delete(TABLE_NAME, COLUMN_TITLE + " = ?", new String[]{title});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return rowsAffected;
    }


}
