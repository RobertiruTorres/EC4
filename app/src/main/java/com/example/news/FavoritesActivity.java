package com.example.news;

import com.example.news.database.DatabaseHelper;
import com.example.news.list.ArticleAdapter;
import com.example.news.models.ArticleModel;
import com.example.news.session.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;

    private List<ArticleModel> itemList;

    private DatabaseHelper databaseHelper;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        sessionManager = new SessionManager(this);
        sessionManager.validateSession();

        databaseHelper = new DatabaseHelper(this);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Favoritos Tesla");

        ImageView actionNews = findViewById(R.id.action_news);
        actionNews.setVisibility(View.VISIBLE);
        actionNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this, ArticlesActivity.class);
                startActivity(intent);
            }
        });

        ImageView actionLogout = findViewById(R.id.action_logout);
        actionLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        ImageView actionFavorite = findViewById(R.id.action_favorite);
        actionFavorite.setVisibility(View.INVISIBLE);
        actionFavorite.getLayoutParams().width = 0;
        actionFavorite.getLayoutParams().height = 0;


        List<ArticleModel> articlesResponse = databaseHelper.getAllFavorites();

        alert(findViewById(R.id.newsContainer), "TOTAL DE ARTICULOS FAVORITOS: " + articlesResponse.size(), Color.WHITE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(FavoritesActivity.this));

        articleAdapter = new ArticleAdapter(FavoritesActivity.this, articlesResponse);
        recyclerView.setAdapter(articleAdapter);

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