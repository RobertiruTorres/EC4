package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.database.DatabaseHelper;
import com.example.news.models.ArticleModel;
import com.example.news.session.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class ArticleActivity extends AppCompatActivity {
    private boolean isHeartFilled;
    private DatabaseHelper databaseHelper;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        sessionManager = new SessionManager(this);
        sessionManager.validateSession();

        databaseHelper = new DatabaseHelper(this);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        ImageView actionLogout = findViewById(R.id.action_logout);
        actionLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        ImageView actionFavorite = findViewById(R.id.action_favorite);
        actionFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticleActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });


        //Intent data
        String author = getIntent().getStringExtra("author");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String url = getIntent().getStringExtra("url");
        String urlToImage = getIntent().getStringExtra("urlToImage");
        String publishedAt = getIntent().getStringExtra("publishedAt");
        String content = getIntent().getStringExtra("content");


        TextView authorText = findViewById(R.id.authorText);
        TextView titleText = findViewById(R.id.articleTitle);
        TextView articleDescription = findViewById(R.id.articleDescription);
        TextView publishedAtText = findViewById(R.id.publishedAtText);
        ImageView articleImage = findViewById(R.id.articleImage);

        authorText.setText(author);
        titleText.setText(title);
        articleDescription.setText(description);
        publishedAtText.setText(publishedAt.split("T")[0]);
        Picasso.get().load(urlToImage).into(articleImage);


        //Heart
        ImageView heartIcon = findViewById(R.id.heartIcon);
        ArticleModel article = databaseHelper.getFavoriteByTitle(title);
        if(article == null){
            //Aun no esta en favoritos
            heartIcon.setImageResource(R.drawable.heart_white);
            isHeartFilled = false;
        }else{
            //Ya esta en esta en favoritos
            heartIcon.setImageResource(R.drawable.heart_red);
            isHeartFilled = true;
        }
        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHeartFilled = !isHeartFilled; // Cambia el estado del corazón al hacer clic
                if (isHeartFilled) {
                    heartIcon.setImageResource(R.drawable.heart_red);
                    // Insertar favorito
                    long newFavorite = databaseHelper.insertFavorite(author, title, description, url, urlToImage, publishedAt, content);
                    if (newFavorite != -1) {
                        alert(findViewById(R.id.appContainer), "Articulo agregado a favoritos.", Color.GREEN);
                    } else {
                        alert(findViewById(R.id.appContainer), "Error al agregar.", Color.RED);
                    }
                } else {
                    heartIcon.setImageResource(R.drawable.heart_white);
                    //Eliminar favorito
                    int rowsDeleted = databaseHelper.deleteFavoriteByTitle(title);
                    if (rowsDeleted > 0) {
                        alert(findViewById(R.id.appContainer), "Articulo eliminado de favoritos.", Color.RED);
                    } else {
                        alert(findViewById(R.id.appContainer), "Error al eliminar.", Color.RED);
                    }
                }

            }
        });
    }
    private void showLogoutConfirmationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    sessionManager.logout();
                    Intent intent = new Intent(this, MainActivity.class);
                    this.startActivity(intent);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void alert(View view, String message, int textColor) {
        int duration = Snackbar.LENGTH_SHORT;
        Snackbar snackbar = Snackbar.make(view, message, duration);

        // Establecer el color del texto del mensaje
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(textColor);

        snackbar.show();
    }


}