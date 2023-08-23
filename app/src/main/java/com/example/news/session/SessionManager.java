package com.example.news.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.news.FavoritesActivity;
import com.example.news.MainActivity;

public class SessionManager {
    private Context mContext;

    public SessionManager(Context context) {
        mContext = context;
    }

    public void createSession(){
        // Guardar el estado de inicio de sesión
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logueado", true);
        editor.apply();
    }
    public void validateSession(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        boolean logueado = sharedPreferences.getBoolean("logueado", false);

        if (!logueado) {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
        }
    }
    public void logout(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Borra todos los datos almacenados en las preferencias compartidas
        editor.apply(); // Aplica los cambios
    }
}
