package com.example.news;
import com.example.news.http.response.ArticlesResponse;
import com.example.news.http.retrofit.ArticlesService;
import com.example.news.http.retrofit.RetrofitHelper;
import com.example.news.list.ArticleAdapter;
import com.example.news.models.ArticleModel;
import com.example.news.session.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private List<ArticleModel> itemList;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        sessionManager = new SessionManager(this);
        sessionManager.validateSession();

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
                Intent intent = new Intent(ArticlesActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });
        // Mostrar el loader
        CircularProgressIndicator loaderCircularProgress = findViewById(R.id.loaderCircularProgress);
        loaderCircularProgress.setVisibility(View.VISIBLE);

        // Ocultar el contenido principal mientras se carga
        findViewById(R.id.newsContainer).setVisibility(View.GONE);



        ArticlesService apiService = RetrofitHelper.getInstance().create(ArticlesService.class);
        Call<ArticlesResponse> call = apiService.getNews();

        call.enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                if (response.isSuccessful()) {

                    // Ocultar el loader
                    loaderCircularProgress.setVisibility(View.GONE);
                    // Mostrar el contenido principal
                    findViewById(R.id.newsContainer).setVisibility(View.VISIBLE);


                    ArticlesResponse articlesResponse = response.body();

                    alert(findViewById(R.id.newsContainer), "TOTAL DE ARTICULOS OBTENIDOS: " + articlesResponse.getArticlesModelList().size(), Color.WHITE);

                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ArticlesActivity.this));

                    articleAdapter = new ArticleAdapter(ArticlesActivity.this, articlesResponse.getArticlesModelList());
                    recyclerView.setAdapter(articleAdapter);

                } else {

                    alert(findViewById(R.id.newsContainer), "ERROR AL CONSUMIR DATOS DE NEWSAPI", Color.RED);
                }
            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                // Manejar el error de la solicitud
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